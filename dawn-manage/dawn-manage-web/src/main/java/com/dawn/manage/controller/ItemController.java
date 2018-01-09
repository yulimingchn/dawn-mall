package com.dawn.manage.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawn.common.bean.EasyUIResult;
import com.dawn.manage.pojo.Item;
import com.dawn.manage.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 新增商品
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc) {
        try {
            // 校验
            if (StringUtils.isEmpty(item.getTitle())) {
                // 参数有误，响应400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // 保存商品数据
            this.itemService.saveItem(item, desc);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 分页查询商品列表，按照更新时间倒序排序
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {
        try {
            EasyUIResult easyUIResult = this.itemService.queryItemList(page, rows);
            if(null == easyUIResult){
                // 参数有误，响应400
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 更新商品
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc) {
        try {
            // 校验
            if (StringUtils.isEmpty(item.getTitle())) {
                // 参数有误，响应400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // 更新商品数据
            this.itemService.upadteItem(item, desc);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
