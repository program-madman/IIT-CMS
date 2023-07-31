package com.iit.project.cms.CMSServer.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_type")
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "type_name", unique = true, nullable = false)
    private String typeName;
}

