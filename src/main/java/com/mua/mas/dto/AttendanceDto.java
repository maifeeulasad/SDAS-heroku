package com.mua.mas.dto;

import com.mua.mas.model.Attendance;
import lombok.Data;

@Data
public class AttendanceDto extends Attendance {

    private Long userId;
    private String username;

    public AttendanceDto(Attendance attendance,Boolean sudoAuth){
        setAttendanceId(attendance.getAttendanceId());
        if(sudoAuth){
            setCode(attendance.getCode());
        }
        setCoordinate(attendance.getCoordinate());
        setState(attendance.getState());
        setUserId(attendance.getUser().getUserId());
        setUsername(attendance.getUser().getUsername());
        setAttendanceTime(attendance.getAttendanceTime());
    }

}
