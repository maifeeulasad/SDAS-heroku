package com.mua.mas.repo;

import com.mua.mas.dto.ClassroomDto;
import com.mua.mas.dto.SessionDto;
import com.mua.mas.model.Classroom;
import com.mua.mas.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepo extends JpaRepository<Classroom,Long> {

    @Query("select new com.mua.mas.dto.ClassroomDto(classroom) " +
            "from Classroom classroom " +
            "join classroom.userClassroomRoleList userclassroomrole " +
            "where userclassroomrole.user.userId=?1 " +
            "order by classroom.nextSessionTime asc")
    List<ClassroomDto> findByUserId(Long userId);

    @Query("select distinct new com.mua.mas.dto.ClassroomDto(classroom) " +
            "from Classroom classroom " +
            "join classroom.userClassroomRoleList userclassroomrole " +
            "where not userclassroomrole.user.userId=?1 " +
            "and classroom.name like %?2%")
    List<ClassroomDto> findByUserIdAndKeyword(Long userId,String keyword);
}
