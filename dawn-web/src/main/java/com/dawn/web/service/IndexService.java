package com.dawn.web.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dawn.common.bean.EasyUIResult;
import com.dawn.common.service.ApiService;
import com.dawn.manage.pojo.Content;

@Service
public class IndexService {

    @Autowired
    private ApiService apiService;

    @Value("${DAWN_MANAGE_URL}")
    private String DAWN_MANAGE_URL;

    @Value("${DAWN_INDEX_AD1}")
    private String DAWN_INDEX_AD1;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 调用后台系统的接口服务获取大广告的数据
     * 
     * @return
     */
    public String queryIndexAD1() {
        try {
            String url = DAWN_MANAGE_URL + DAWN_INDEX_AD1;
            String jsonData = this.apiService.doGet(url);
            EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Content.class);

            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            for (Content content : (List<Content>) easyUIResult.getRows()) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("srcB", content.getPic());
                map.put("height", 240);
                map.put("alt", content.getTitle());
                map.put("width", 670);
                map.put("src", content.getPic());
                map.put("widthB", 550);
                map.put("href", content.getUrl());
                map.put("heightB", 240);

                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 给出一个默认的广告数据 TODO
        return null;
    }

    // public String queryIndexAD1() {
    // try {
    // String url = TAOTAO_MANAGE_URL + TAOTAO_INDEX_AD1;
    // String jsonData = this.apiService.doGet(url);
    // // 解析json
    // JsonNode jsonNode = MAPPER.readTree(jsonData);
    // ArrayNode rows = (ArrayNode) jsonNode.get("rows");
    // List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    // for (JsonNode row : rows) {
    // Map<String, Object> map = new LinkedHashMap<String, Object>();
    // map.put("srcB", row.get("pic").asText());
    // map.put("height", 240);
    // map.put("alt", row.get("title").asText());
    // map.put("width", 670);
    // map.put("src", row.get("pic").asText());
    // map.put("widthB", 550);
    // map.put("href", row.get("url").asText());
    // map.put("heightB", 240);
    //
    // result.add(map);
    // }
    // return MAPPER.writeValueAsString(result);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // // 给出一个默认的广告数据 TODO
    // return null;
    // }

}
