package com.dawn.web.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dawn.common.service.ApiService;
import com.dawn.web.bean.Cart;
import com.dawn.web.bean.User;
import com.dawn.web.threadlocal.UserThreadLocal;

@Service
public class CartService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_CART_URL}")
    private String TAOTAO_CART_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public List<Cart> queryCartList() {
        User user = UserThreadLocal.get();
        // 调用购物车系统提供的接口服务获取数据
        try {
            String url = TAOTAO_CART_URL + "/service/api/cart/" + user.getId();
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                return MAPPER.readValue(jsonData,
                        MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
