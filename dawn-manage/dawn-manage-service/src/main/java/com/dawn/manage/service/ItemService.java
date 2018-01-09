package com.dawn.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dawn.common.bean.EasyUIResult;
import com.dawn.common.service.ApiService;
import com.dawn.manage.mapper.ItemMapper;
import com.dawn.manage.pojo.Item;
import com.dawn.manage.pojo.ItemDesc;

@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ApiService apiService;
    
    @Autowired
    private PropertieService propertieService;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public void saveItem(Item item, String desc) {
        // 设置一些初始值
        item.setStatus(1);
        // 强制设置id为null（所有不能被用户提交的数据）
        item.setId(null);

        // 保存商品的数据基本数据
        super.save(item);

        // 保存商品描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        this.itemDescService.save(itemDesc);
        
        sendMsg(item.getId(), "insert");

    }

    /**
     * 查询列表
     * 
     * TOO： 将该查询方法封装到BaseService中
     * 
     * @param page
     * @param rows
     * @return
     */
    public EasyUIResult queryItemList(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Item.class);
        example.setOrderByClause("updated DESC");// 按照更新时间倒序排序
        List<Item> list = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<Item>(list);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

    public void upadteItem(Item item, String desc) {
        // 强制设置不能被更新的字段为null
        item.setStatus(null);
        super.updateSelective(item);

        // 更新商品描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        this.itemDescService.updateSelective(itemDesc);

//        try {
//            // 通知其他系统，该商品已经被更新
//            String url = this.propertieService.TAOTAO_WEB_URL + "/item/cache/" + item.getId() + ".html";
//            this.apiService.doPost(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
        sendMsg(item.getId(), "update");
    }
    
    private void sendMsg(Long itemId, String type){
        try {
            Map<String, Object> msg = new HashMap<String, Object>();
            msg.put("itemId", itemId);
            msg.put("type", type);
            msg.put("date", System.currentTimeMillis());
            
            // 通知其他系统，发送消息
            this.rabbitTemplate.convertAndSend("item." + type, MAPPER.writeValueAsString(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
