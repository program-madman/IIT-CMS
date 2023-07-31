package com.iit.project.cms.CMSServer.entity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "category")
public class Category implements Serializable {
    @Id
    @Column(name = "category_name", length = 100, nullable = false)
    private String categoryName;
}

