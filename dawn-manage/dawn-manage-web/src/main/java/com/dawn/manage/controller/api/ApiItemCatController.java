package com.dawn.manage.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dawn.common.bean.ItemCatResult;
import com.dawn.manage.service.ItemCatService;

/**
 *
 *  对外提供接口服务，查询商品类目的所有数据
 *
 */
@RequestMapping("api/item/cat")
@Controller
public class ApiItemCatController {

    @Autowired
    private ItemCatService itemCatService;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity<String> apiQueryItemCat(
//            @RequestParam(value = "callback", required = false) String callback) {
//        try {
//            ItemCatResult itemCatResult = this.itemCatService.apiQueryItemCat();
//            
//            String json = MAPPER.writeValueAsString(itemCatResult);
//            if(StringUtils.isEmpty(callback)){
//                return ResponseEntity.ok(json);
//            }
//            return ResponseEntity.ok(callback + "("+json+")");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> apiQueryItemCat() {
        try {
            ItemCatResult itemCatResult = this.itemCatService.apiQueryItemCat();
            return ResponseEntity.ok(itemCatResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
