package com.iit.project.cms.CMSServer.entity.permission;
import lombok.Data;

import java.io.Serializable;
@Data
public class RoleFunctionId implements Serializable {
    private Long roleId;
    private Long functionId;

}

