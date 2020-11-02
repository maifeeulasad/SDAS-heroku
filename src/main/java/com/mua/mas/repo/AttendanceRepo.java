package com.mua.mas.repo;

import com.mua.mas.dto.AttendanceDto;
import com.mua.mas.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance,Long> {

    @Query("select distinct new com.mua.mas.dto.AttendanceDto(attendance,false) from Attendance attendance where attendance.session.sessionId=?1")
    List<AttendanceDto> findBySessionId(Long sessionId);

    @Query("select distinct new com.mua.mas.dto.AttendanceDto(attendance,true) from Attendance attendance where attendance.session.sessionId=?1")
    List<AttendanceDto> findBySessionIdSuper(Long sessionId);


}
