package com.mua.mas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAttendance {

    private Long classroomId;
    private Long attendanceAccepted;
    private Long attendanceRejected;
    private Long attendanceAutoAccepted;
    private Long attendanceAutoRejected;


}
