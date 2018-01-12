package com.dawn.search.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dawn.common.service.ApiService;
import com.dawn.search.bean.Item;

@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${DAWN_MANAGE_URL}")
    private String DAWN_MANAGE_URL;

    public Item queryItemById(Long itemId) {
        try {
            String url = DAWN_MANAGE_URL + "/rest/api/item/" + itemId;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                return MAPPER.readValue(jsonData, Item.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
