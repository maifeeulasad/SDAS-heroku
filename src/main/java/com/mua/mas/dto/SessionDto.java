package com.mua.mas.dto;

import com.mua.mas.model.Session;
import lombok.Data;

@Data
public class SessionDto extends Session {

    private Long classroomId;

    public SessionDto(Session session){
        setSessionId(session.getSessionId());
        setWeight(session.getWeight());
        setMinutes(session.getMinutes());
        setCode(session.getCode());
        setCreationTime(session.getCreationTime());
        setBounds(session.getBounds());
        setClassroom(null);
        setClassroomId(session.getClassroom().getClassroomId());
        setAttendances(null);
    }

    public SessionDto(Session session,Boolean superAuthority){
        setSessionId(session.getSessionId());
        setWeight(session.getWeight());
        setMinutes(session.getMinutes());
        if(superAuthority){
            setCode(session.getCode());
        }
        setCreationTime(session.getCreationTime());
        setBounds(session.getBounds());
        setClassroom(null);
        setClassroomId(session.getClassroom().getClassroomId());
        setAttendances(null);
    }

}
