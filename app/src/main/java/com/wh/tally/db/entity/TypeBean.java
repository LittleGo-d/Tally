package com.wh.tally.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 表示收入或支出具体类型的类
 */
@Entity(tableName = "type")
public class TypeBean {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String typeName; // 类型 如烟酒
    private Integer imageId; // 未被选中图片id
    private Integer sImageId; // 被选中图片id
    private Integer kind; // 收入：1 支出：0

    public TypeBean(Integer id,String typeName, Integer imageId, Integer sImageId, Integer kind) {
        this.id = id;
        this.typeName = typeName;
        this.imageId = imageId;
        this.sImageId = sImageId;
        this.kind = kind;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getSImageId() {
        return sImageId;
    }


    public void setSImageId(Integer sImageId) {
        this.sImageId = sImageId;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }
}
