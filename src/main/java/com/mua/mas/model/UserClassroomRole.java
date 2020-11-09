package com.mua.mas.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class UserClassroomRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userClassroomRoleId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Classroom classroom;


    @Column(updatable = false)
    @CreationTimestamp
    private Date joiningDate;

    public UserClassroomRole(User user,Role role,Classroom classroom){
        this.user = user;
        this.role = role;
        this.classroom = classroom;
    }

}
