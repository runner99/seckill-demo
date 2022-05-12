package com.xxxx.seckill.controller;

import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.GoodsService;
import com.xxxx.seckill.service.UserService;
import com.xxxx.seckill.vo.DetailVo;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 跳转到商品列表页
     */
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model,User user,
                         HttpServletRequest request, HttpServletResponse response){
        //redis获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String)valueOperations.get("goodsList");

        if (!StringUtils.isEmpty(html)){
            return html;
        }

        model.addAttribute("user",user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
//        return "goodsList";
        //如果为空，手动渲染，存入redis并返回
        WebContext context = new WebContext(request,response
                ,request.getServletContext(),request.getLocale()
                ,model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    /**
     * 跳转商品详情页
     * @param goodsId
     * @return
     */
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model, User user, @PathVariable Long goodsId){

       GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowdate = new Date();
        //秒杀状态
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;

        //秒杀还未开始
        if (nowdate.before(startDate)){
            remainSeconds = ((int)((startDate.getTime()-nowdate.getTime())/1000));

        }else if (nowdate.after(endDate)){
            //秒杀结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else {
            //秒杀中
            seckillStatus = 1;
            remainSeconds = 0;
        }

        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);

        return RespBean.success(detailVo);

    }
    /**
     * 跳转商品详情页
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/toDetail2/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model,User user,@PathVariable Long goodsId
            ,HttpServletRequest request, HttpServletResponse response){

        ValueOperations valueOperations = redisTemplate.opsForValue();
        //redis中获取页面，如果不为空，直接返回页面
        String html = (String)valueOperations.get("goodsDetail:" + goodsId);
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowdate = new Date();
        //秒杀状态
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;

        //秒杀还未开始
        if (nowdate.before(startDate)){
            remainSeconds = ((int)((startDate.getTime()-nowdate.getTime())/1000));

        }else if (nowdate.after(endDate)){
            //秒杀结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else {
            //秒杀中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("seckillStatus",seckillStatus);
        model.addAttribute("goods",goodsVo);
//        return "goodsDetail";

        //如果为空，手动渲染，存入redis并返回
        WebContext context = new WebContext(request,response
                ,request.getServletContext(),request.getLocale()
                ,model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail:"+goodsId,html,60, TimeUnit.SECONDS);
        }
        return html;

    }


}
