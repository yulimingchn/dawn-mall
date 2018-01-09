package com.dawn.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dawn.manage.pojo.BasePojo;

public abstract class BaseService<T extends BasePojo> {

    @Autowired
    private Mapper<T> mapper;

    // public abstract Mapper<T> getMaper();

    /**
     * 根据主键id查询数据
     * 
     * @param id
     * @return
     */
    public T queryById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有的数据
     * 
     * @return
     */
    public List<T> queryAll() {
        return this.mapper.select(null);
    }

    /**
     * 根据条件查询一条数据
     * 
     * @param record
     * @return
     */
    public T queryOne(T record) {
        return this.mapper.selectOne(record);
    }

    /**
     * 根据条件查询数据列表
     * 
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record) {
        return this.mapper.select(record);
    }

    /**
     * 根据条件分页查询数据
     * 
     * @param record
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T record, Integer page, Integer rows) {
        // 设置分页参数
        PageHelper.startPage(page, rows);
        List<T> list = this.queryListByWhere(record);
        return new PageInfo<T>(list);
    }

    /**
     * 新增数据
     * 
     * @param record
     * @return
     */
    public Integer save(T record) {
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return this.mapper.insert(record);
    }

    /**
     * 新增数据(选择不为null的属性，作为插入字段进行插入操作)
     * 
     * @param record
     * @return
     */
    public Integer saveSelective(T record) {
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return this.mapper.insertSelective(record);
    }

    /**
     * 更新数据
     * 
     * @param record
     * @return
     */
    public Integer update(T record) {
        record.setUpdated(new Date());
        // TODO 处理传入created时间的问题
        return this.mapper.updateByPrimaryKey(record);
    }

    /**
     * 更新数据(选择不为null的属性，作为更新的字段，进行操作)
     * 
     * @param record
     * @return
     */
    public Integer updateSelective(T record) {
        record.setUpdated(new Date());
        record.setCreated(null);
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除id
     * 
     * @param id
     * @return
     */
    public Integer deleteById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除
     * 
     * @param id
     * @return
     */
    public Integer deleteByIds(List<Object> ids, Class<T> clazz, String property) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, ids);
        return this.mapper.deleteByExample(example);
    }

    /**
     * 根据主键删除id
     * 
     * @param id
     * @return
     */
    public Integer deleteByWhere(T record) {
        return this.mapper.delete(record);
    }

}
