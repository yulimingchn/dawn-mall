package com.dawn.common.service;

/**
 * @author dawn
 * @param <T>
 * @param <E>
 * 接口回调函数
 */
public interface Function<T, E> {
    /**
     * 回调函数
     * @param e
     * @return
     */
     T callback(E e);

}
