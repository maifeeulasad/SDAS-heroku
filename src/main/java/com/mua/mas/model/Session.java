package com.mua.mas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sessionId;

    @Column(nullable = false)
    private Long weight;
    @Column(nullable = false)
    private Long minutes;
    @Column(length = 8,nullable = false)
    private String code;


    @CreationTimestamp
    @Column(updatable = false)
    private Date creationTime;

    @ElementCollection
    @CollectionTable(name = "bounds", joinColumns = @JoinColumn(name = "session_id"))
    @Column(nullable = false)
    private List<Coordinate> bounds = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Classroom classroom;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Attendance> attendances = new ArrayList<>();

}
