package com.dawn.web.service;

import com.dawn.common.httpclient.HttpResult;
import com.dawn.common.service.ApiService;
import com.dawn.web.bean.Order;
import com.dawn.web.bean.User;
import com.dawn.web.threadlocal.UserThreadLocal;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private ApiService apiService;

    @Value("${DAWN_ORDER_URL}")
    private String DAWN_ORDER_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String submitOrder(Order order) {

        User user = UserThreadLocal.get();
        order.setBuyerNick(user.getUsername());
        order.setUserId(user.getId());

        try {
            String url = DAWN_ORDER_URL + "/order/create";
            HttpResult httpResult = this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
            // 响应状态码
            if (httpResult.getCode().intValue() == 200) {
                String data = httpResult.getData();
                JsonNode jsonNode = MAPPER.readTree(data);
                // 业务状态码
                if (jsonNode.get("status").intValue() == 200) {
                    // 订单提交成功
                    return jsonNode.get("data").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order querOrderById(String orderId) {
        String url = DAWN_ORDER_URL + "/order/query/" + orderId;
        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isNotEmpty(jsonData)){
                return MAPPER.readValue(jsonData, Order.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
