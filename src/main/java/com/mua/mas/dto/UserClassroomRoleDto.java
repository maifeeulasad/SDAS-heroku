package com.mua.mas.dto;

import com.mua.mas.model.Role;
import com.mua.mas.model.UserClassroomRole;
import lombok.Data;

@Data
public class UserClassroomRoleDto extends UserClassroomRole {

    private Long userClassroomRoleId;
    private Long userId;
    private String username;
    private Role role;

    public UserClassroomRoleDto(UserClassroomRole userClassroomRole){
        setUserClassroomRoleId(userClassroomRole.getUserClassroomRoleId());
        setUserId(userClassroomRole.getUser().getUserId());
        setUsername(userClassroomRole.getUser().getUsername());
        setRole(userClassroomRole.getRole());
    }

}
