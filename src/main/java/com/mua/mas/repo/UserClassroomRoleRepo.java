package com.mua.mas.repo;

import com.mua.mas.dto.UserClassroomRoleDto;
import com.mua.mas.model.UserClassroomRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserClassroomRoleRepo extends JpaRepository<UserClassroomRole,Long> {
    Optional<UserClassroomRole> findByUser_UserIdAndClassroom_ClassroomId(Long userId, Long classroomId);
    @Query("select distinct new com.mua.mas.dto.UserClassroomRoleDto(ucr) from UserClassroomRole ucr where ucr.classroom.classroomId = ?1")
    List<UserClassroomRoleDto> findByClassroom_ClassroomId(Long classroomId);

}
