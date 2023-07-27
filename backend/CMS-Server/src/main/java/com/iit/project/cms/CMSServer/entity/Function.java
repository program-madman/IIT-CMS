package com.iit.project.cms.CMSServer.entity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "function")
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "function_id")
    private Long functionId;

    @Column(name = "function_name", nullable = false)
    private String functionName;

    @Column(name = "api", nullable = false)
    private String api;
}

