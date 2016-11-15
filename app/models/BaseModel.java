package models;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/4.
 */
@MappedSuperclass
public class BaseModel extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Date creationTime;//创建时间
    public Date updateTime;//更新时间

    public Long getId() {
        return id;
    }

    @Override
    public Object _key() {
        return getId();
    }

    @PreUpdate
    public void preUpdateTime() {
        updateTime = new Date();
    }

    @PrePersist
    public void prePersistTime() {
        creationTime = new Date();
        updateTime = new Date();
    }
}
