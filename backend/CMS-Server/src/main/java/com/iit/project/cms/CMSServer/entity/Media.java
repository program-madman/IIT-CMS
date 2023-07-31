package com.iit.project.cms.CMSServer.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "media")
@Data
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long attachmentId;

    @Column(name = "save_name", nullable = false)
    private String saveName;

    @Column(name = "file_key", nullable = false)
    private String fileKey;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "save_time", nullable = false)
    private Date saveTime;


}

