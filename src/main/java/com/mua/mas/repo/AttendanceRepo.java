package com.mua.mas.repo;

import com.mua.mas.dto.AttendanceDto;
import com.mua.mas.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance,Long> {

    @Query("select distinct new com.mua.mas.dto.AttendanceDto(attendance,false)" +
            " from Attendance attendance" +
            " where attendance.session.sessionId=?1")
    List<AttendanceDto> findBySessionId(Long sessionId);

    @Query("select distinct new com.mua.mas.dto.AttendanceDto(attendance,true)" +
            " from Attendance attendance" +
            " where attendance.session.sessionId=?1")
    List<AttendanceDto> findBySessionIdSuper(Long sessionId);

    /*
    @Query("select attendance from Attendance attendance join attendance.session join")
    List<UserAttendance> attendancesOfUserByClassroom(Long userId);
    */

    /*SELECT
CLASSROOM_CLASSROOM_ID,
COUNT(CASE WHEN STATE='Accepted' THEN 1 END) AS ACCEPTED,
COUNT(CASE WHEN STATE='Rejected' THEN 1 END) AS REJECTED,
COUNT(CASE WHEN STATE='AutoAccepted' THEN 1 END) AS AUTO_ACCEPTED,
COUNT(CASE WHEN STATE='AutoRejected' THEN 1 END) AS AUTO_REJECTED,
COUNT(DISTINCT SESSION.SESSION_ID) AS TOTAL_SESSION
FROM ATTENDANCE
INNER JOIN SESSION ON SESSION_SESSION_ID = SESSION.SESSION_ID
INNER JOIN CLASSROOM ON CLASSROOM.CLASSROOM_ID = CLASSROOM_CLASSROOM_ID
WHERE USER_USER_ID = 8
GROUP BY CLASSROOM_CLASSROOM_ID
//still total session count problem, need to query on classroom i guess
     */

}
