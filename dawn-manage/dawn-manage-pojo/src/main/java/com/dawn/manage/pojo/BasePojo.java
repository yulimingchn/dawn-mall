package com.dawn.manage.pojo;

import java.util.Date;

/**
 * @author dawn
 */
public abstract class BasePojo {
    
    private Date created;
    private Date updated;
    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
    public Date getUpdated() {
        return updated;
    }
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    
    

}
