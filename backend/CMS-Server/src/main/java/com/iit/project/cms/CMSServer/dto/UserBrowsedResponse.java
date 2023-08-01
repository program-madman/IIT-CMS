package com.iit.project.cms.CMSServer.dto;

import com.iit.project.cms.CMSServer.entity.Article;
import lombok.Data;

import java.util.Date;

/**
 * @author Daniel
 * @date 2023年08月01日 12:58
 */
@Data
public class UserBrowsedResponse {


    private String userName;

    private Date browseTime;

    private String categoryName;

    private String title;

    private String content;

    private Date publishTime;

    private Date updateTime;



}
