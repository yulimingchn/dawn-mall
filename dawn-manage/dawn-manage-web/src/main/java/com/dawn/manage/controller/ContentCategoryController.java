package com.dawn.manage.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawn.manage.pojo.ContentCategory;
import com.dawn.manage.service.ContentCategoryService;

@RequestMapping("content/category")
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 查询内容分类列表
     * 
     * @param pid
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryContentCategoryByPid(
            @RequestParam(value = "id", defaultValue = "0") Long pid) {
        try {
            ContentCategory record = new ContentCategory();
            record.setParentId(pid);
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
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

    /**
     * 新增节点
     * 
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(@RequestParam("parentId") Long parentId,
            @RequestParam("name") String name) {
        try {
            ContentCategory contentCategory = new ContentCategory();
            contentCategory.setParentId(parentId);
            contentCategory.setName(name);
            contentCategory.setSortOrder(1);
            contentCategory.setStatus(1);
            contentCategory.setIsParent(false);
            this.contentCategoryService.save(contentCategory);

            // 判断该节点的父节点的isParent是否为true，如果不是，需要修改为true
            ContentCategory parent = this.contentCategoryService.queryById(parentId);
            if (!parent.getIsParent()) {
                parent.setIsParent(true);
                this.contentCategoryService.update(parent);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 重命名
     * 
     * @param id
     * @param name
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> rename(@RequestParam("id") Long id, @RequestParam("name") String name) {
        try {
            ContentCategory record = new ContentCategory();
            record.setId(id);
            record.setName(name);
            this.contentCategoryService.updateSelective(record);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 删除节点，包含它的所有的子节点
     * 
     * TODO: 解决事务问题
     * 
     * @param contentCategory
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(ContentCategory contentCategory) {
        try {
            // 收集待删除的所有的节点id
            List<Object> ids = new ArrayList<Object>();
            ids.add(contentCategory.getId());

            // 递归查询该节点的所有子节点
            findAllSubNode(contentCategory.getId(), ids);

            this.contentCategoryService.deleteByIds(ids, ContentCategory.class, "id");

            // 判断该节点是否存在其他的兄弟节点，如果不存在，需要修改父节点的isParent为false
            ContentCategory record = new ContentCategory();
            record.setParentId(contentCategory.getParentId());
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
            if (CollectionUtils.isEmpty(list)) {
                ContentCategory parent = new ContentCategory();
                parent.setId(contentCategory.getParentId());
                parent.setIsParent(false);
                this.contentCategoryService.updateSelective(parent);
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private void findAllSubNode(Long pid, List<Object> ids) {
        ContentCategory record = new ContentCategory();
        record.setParentId(pid);
        List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (ContentCategory contentCategory : list) {
            ids.add(contentCategory.getId());
            // 判断该节点是否为父节点，如果为父节点需要递归查询
            if (contentCategory.getIsParent()) {
                findAllSubNode(contentCategory.getId(), ids);
            }
        }
    }

}
