package com.dawn.web.bean;

import org.apache.commons.lang3.StringUtils;

public class Item extends com.dawn.manage.pojo.Item {

    public String[] getImages() {
        return StringUtils.split(super.getImage(), ',');
    }

}
