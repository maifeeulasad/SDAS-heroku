package com.mua.mas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long classroomId;

    @Column(nullable = false)
    private String name;
    private String details;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserClassroomRole> userClassroomRoleList=new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();

    @JsonFormat(pattern="MMM d, yyyy h:mm:ss a")
    private Date nextSessionTime;

}
