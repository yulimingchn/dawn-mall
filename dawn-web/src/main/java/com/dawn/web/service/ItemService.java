package com.dawn.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dawn.common.service.ApiService;
import com.dawn.common.service.RedisService;
import com.dawn.manage.pojo.ItemDesc;
import com.dawn.web.bean.Item;

@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    @Value("${DAWN_MANAGE_URL}")
    private String DAWN_MANAGE_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    private static final Integer REDIS_TIME = 60 * 60 * 24;

    public static final String REDIS_KEY = "DAWN_WEB_ITEM_DETAIL_";

    public Item queryItemById(Long itemId) {

        // 先从缓存中命中，如果命中，就返回，没有命中，继续执行后续的逻辑
        String key = REDIS_KEY + itemId;
        try {
            String cacheData = this.redisService.get(key);
            if (StringUtils.isNotEmpty(cacheData)) {
                // 命中
                return MAPPER.readValue(cacheData, Item.class);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = DAWN_MANAGE_URL + "/rest/api/item/" + itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {

                try {
                    // 将查询的数据写入到缓存中
                    this.redisService.set(key, jsonData, REDIS_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return MAPPER.readValue(jsonData, Item.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemDesc queryItemDescByItemId(Long itemId) {
        String url = DAWN_MANAGE_URL + "/rest/api/item/desc/" + itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                return MAPPER.readValue(jsonData, ItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
