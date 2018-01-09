package com.dawn.manage.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawn.manage.pojo.ItemCat;
import com.dawn.manage.service.ItemCatService;

@RequestMapping("item/cat")
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> queryItemCatListByParentId(
            @RequestParam(value = "id", defaultValue = "0") Long pid) {
        try {
            ItemCat record = new ItemCat();
            record.setParentId(pid);
            List<ItemCat> list = this.itemCatService.queryListByWhere(record);
            if (CollectionUtils.isEmpty(list)) {
                // 资源不存在，响应404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
