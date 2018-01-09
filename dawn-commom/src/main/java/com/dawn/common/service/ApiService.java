package com.dawn.common.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dawn.common.httpclient.HttpResult;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ApiService implements BeanFactoryAware {

    @Autowired(required = false)
    private RequestConfig requestConfig;

    private BeanFactory beanFactory;

    /**
     * 执行GET请求
     * 
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doGet(String url) throws ClientProtocolException, IOException {
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(this.requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = getHttpclient().execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * 带有参数的GET请求
     * 
     * @param url
     * @param param
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws URISyntaxException
     */
    public String doGet(String url, Map<String, String> param) throws ClientProtocolException, IOException,
            URISyntaxException {
        URIBuilder builder = new URIBuilder(url);
        for (Map.Entry<String, String> entry : param.entrySet()) {
            builder.setParameter(entry.getKey(), entry.getValue());
        }
        return this.doGet(builder.build().toString());
    }

    /**
     * 执行带有参数的POST请求
     * 
     * @param url
     * @param param
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResult doPost(String url, Map<String, String> param) throws ClientProtocolException,
            IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);

        httpPost.setConfig(this.requestConfig);

        if (null != param) {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
            for (Map.Entry<String, String> entry : param.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = getHttpclient().execute(httpPost);

            HttpEntity entity = response.getEntity();
            if (null == entity) {
                return new HttpResult(response.getStatusLine().getStatusCode(), null);
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                    response.getEntity(), "UTF-8"));
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
    
    /**
     * 执行带有参数的POST请求
     * 
     * @param url
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResult doPostJson(String url, String json) throws ClientProtocolException,
            IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);

        httpPost.setConfig(this.requestConfig);

        if (null != json) {
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = getHttpclient().execute(httpPost);

            HttpEntity entity = response.getEntity();
            if (null == entity) {
                return new HttpResult(response.getStatusLine().getStatusCode(), null);
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                    response.getEntity(), "UTF-8"));
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 无参的POST请求
     * 
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResult doPost(String url) throws ClientProtocolException, IOException {
        return this.doPost(url, null);
    }

    private CloseableHttpClient getHttpclient() {
        return this.beanFactory.getBean(CloseableHttpClient.class);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
