package org.throwable.entity;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * @author zhangjinci
 * @version 2017/2/4 10:51
 * @function
 */
@Table(value = "TB_AT_USER")
public class User {

    @Id
    @Column(value = "ID")
    private Integer id;
    @Column(value = "NAME")
    private String name;
    @Column(value = "BIRTH")
    private Date birth;
    @Column(value = "AGE")
    private Integer age;
    @Column(value = "EMAIL")
    private String email;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
