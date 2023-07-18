package com.iit.project.cms.CMSServer.entity.permission;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "role_function")
@IdClass(RoleFunctionId.class)
public class RoleFunction implements Serializable {
    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Id
    @Column(name = "function_id")
    private Long functionId;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "function_id", insertable = false, updatable = false)
    private Function function;

    // Constructors, getters, setters, and other properties/methods
    // Omitted for brevity
}


