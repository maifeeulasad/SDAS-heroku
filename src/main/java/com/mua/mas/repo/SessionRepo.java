package com.mua.mas.repo;

import com.mua.mas.dto.SessionDto;
import com.mua.mas.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepo extends JpaRepository<Session,Long> {

    @Query("select distinct new com.mua.mas.dto.SessionDto(session) from Session session where session.classroom.classroomId = ?1")
    List<SessionDto> findByClassroomId(Long classroomId);

    @Query("select distinct new com.mua.mas.dto.SessionDto(session,false) from Session session where session.classroom.classroomId = ?1")
    List<SessionDto> findByClassroomId(Long classroomId,Boolean superAuthority);

}
