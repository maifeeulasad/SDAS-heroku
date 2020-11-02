package com.mua.mas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(unique = true,nullable = false)
    private String username;
    @Column(updatable = false)
    @CreationTimestamp
    private Date joiningDate;

    @Column(nullable = false)
    private String name;
    private String email;
    @ElementCollection
    @CollectionTable(name = "contacts", joinColumns = @JoinColumn(name = "user_id"))
    private List<String> contacts = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private LoginCredential loginCredential;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserClassroomRole> userClassroomRoles = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Attendance> attendances = new ArrayList<>();

}
