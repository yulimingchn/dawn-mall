package com.dawn.search.mq.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dawn.search.bean.Item;
import com.dawn.search.service.ItemService;

public class ItemMQHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private HttpSolrServer httpSolrServer;

    @Autowired
    private ItemService itemService;

    public void execute(String msg) {
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            Long itemId = jsonNode.get("itemId").asLong();
            String type = jsonNode.get("type").asText();
            if (StringUtils.equals("update", type) || StringUtils.equals("insert", type)) {
                Item item = this.itemService.queryItemById(itemId);
                if (null == item) {
                    return;
                }
                this.httpSolrServer.addBean(item);
                this.httpSolrServer.commit();
            } else if (StringUtils.equals("delete", type)) {
                this.httpSolrServer.deleteById(String.valueOf(itemId));
                this.httpSolrServer.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
