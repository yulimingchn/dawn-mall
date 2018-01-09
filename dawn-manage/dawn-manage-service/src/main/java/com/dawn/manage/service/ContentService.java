package com.dawn.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dawn.common.bean.EasyUIResult;
import com.dawn.manage.mapper.ContentMapper;
import com.dawn.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content> {

    @Autowired
    private ContentMapper contentMapper;

    public EasyUIResult queryContentListByCategoryId(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Content.class);
        example.setOrderByClause("updated DESC");
        example.createCriteria().andEqualTo("categoryId", categoryId);
        List<Content> contents = this.contentMapper.selectByExample(example);
        PageInfo<Content> pageInfo = new PageInfo<Content>(contents);

        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

}
