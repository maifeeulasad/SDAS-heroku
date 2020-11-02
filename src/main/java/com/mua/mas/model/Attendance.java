package com.mua.mas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long attendanceId;

    @Column(length = 8,nullable = false)
    private String code;
    @Column(nullable = false)
    @Embedded
    private Coordinate coordinate;

    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Session session;

    @CreationTimestamp
    @Column(updatable = false)
    private Date attendanceTime;
}
