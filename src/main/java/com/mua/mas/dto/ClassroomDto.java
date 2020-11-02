package com.mua.mas.dto;

import com.mua.mas.model.Classroom;

public class ClassroomDto extends Classroom {

    public ClassroomDto(Classroom classroom){
        setClassroomId(classroom.getClassroomId());
        setName(classroom.getName());
        setDetails(classroom.getDetails());
        setSessions(null);
        setUserClassroomRoleList(null);
        setNextSessionTime(classroom.getNextSessionTime());
    }

}
