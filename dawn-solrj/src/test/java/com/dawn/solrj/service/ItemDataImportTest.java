package com.dawn.solrj.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.junit.Before;
import org.junit.Test;

import com.dawn.solrj.pojo.Item;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class ItemDataImportTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private HttpSolrServer httpSolrServer;

    @Before
    public void setUp() throws Exception {
        // 在url中指定core名称：taotao
        String url = "http://solr.taotao.com/taotao";
        HttpSolrServer httpSolrServer = new HttpSolrServer(url); // 定义solr的server
        httpSolrServer.setParser(new XMLResponseParser()); // 设置响应解析器
        httpSolrServer.setMaxRetries(1); // 设置重试次数，推荐设置为1
        httpSolrServer.setConnectionTimeout(500); // 建立连接的最长时间

        this.httpSolrServer = httpSolrServer;
    }

    @Test
    public void start() throws Exception {

        // 查询数据库中的商品数据
        /**
         * 思路：
         *  1、通过jdbc连接到数据库，查询数据
         *  2、通过后台系统提供的接口服务获取数据（推荐）
         */

        int page = 1;
        int pageSize = 0;
        String url = "http://manage.taotao.com/rest/item?page={page}&rows=100";
        do {
            String jsonData = doGet(StringUtils.replace(url, "{page}", page + ""));
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            ArrayNode rows = (ArrayNode) jsonNode.get("rows");

            List<Item> items = MAPPER.readValue(rows.toString(), MAPPER.getTypeFactory()
                    .constructCollectionType(List.class, Item.class));
            pageSize = items.size();

            // 通过solrj的api将数据写入到solr中
            this.httpSolrServer.addBeans(items);
            this.httpSolrServer.commit();

            page++;

        } while (pageSize == 100);

    }

    private String doGet(String url) throws Exception {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
        return null;
    }

}
