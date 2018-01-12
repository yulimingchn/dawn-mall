package com.dawn.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dawn.web.bean.Cart;
import com.dawn.web.bean.Item;
import com.dawn.web.bean.Order;
import com.dawn.web.service.CartService;
import com.dawn.web.service.ItemService;
import com.dawn.web.service.OrderService;

@RequestMapping("order")
@Controller
public class OrderController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CartService cartService;

    /**
     * 去订单确认页
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("order");
        Item item = this.itemService.queryItemById(itemId);
        mv.addObject("item", item);
        return mv;
    }
    
    /**
     *  基于购物车的订单确认页
     *  
     * @param itemId
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView toOrderCart() {
        ModelAndView mv = new ModelAndView("order-cart");
        List<Cart> carts = this.cartService.queryCartList();
        mv.addObject("carts", carts);
        return mv;
    }

    /**
     * 提交订单
     * 
     * @param order
     * @param token
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitOrder(Order order) {
        Map<String, Object> result = new HashMap<String, Object>();

        String orderId = this.orderService.submitOrder(order);
        if (StringUtils.isEmpty(orderId)) {
            result.put("status", 300); // 订单提交失败
        } else {
            // 订单提交成功
            result.put("status", 200);
            result.put("data", orderId);
        }
        return result;
    }

    /**
     * 成功页
     * 
     * @param orderId
     * @return
     */
    @RequestMapping(value = "success", method = RequestMethod.GET)
    public ModelAndView success(@RequestParam("id") String orderId) {
        ModelAndView mv = new ModelAndView("success");
        Order order = this.orderService.querOrderById(orderId);
        mv.addObject("order", order);
        // 送货时间，当前时间向后推2天，格式：04月20日
        mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }

}
