package com.dawn.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dawn.common.service.ApiService;
import com.dawn.web.bean.User;

@Service
public class UserService {

    @Autowired
    private ApiService apiService;

    @Value("${DAWN_SSO_URL}")
    public String DAWN_SSO_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public User queryUserByToken(String token) {
        try {
            String url = DAWN_SSO_URL + "/service/user/" + token;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                return MAPPER.readValue(jsonData, User.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
