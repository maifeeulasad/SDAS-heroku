package com.mua.mas.service;

import com.mua.mas.dto.AttendanceDto;
import com.mua.mas.model.*;
import com.mua.mas.repo.AttendanceRepo;
import com.mua.mas.repo.SessionRepo;
import com.mua.mas.repo.UserClassroomRoleRepo;
import com.mua.mas.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.*;

@Service
public class AttendanceService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AttendanceRepo attendanceRepo;
    @Autowired
    private SessionRepo sessionRepo;
    @Autowired
    private UserClassroomRoleRepo userClassroomRoleRepo;

    public Boolean attend(Long sessionId, Attendance attendance) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(userPrincipal.getUsername());
            if(!optionalUser.isPresent()){
                return false;
            }
            User user = optionalUser.get();
            Optional<Session> optionalSession = sessionRepo.findById(sessionId);
            if(!optionalSession.isPresent()){
                return false;
            }
            Session session = optionalSession.get();
            Long classroomId = session.getClassroom().getClassroomId();
            Optional<UserClassroomRole> optionalUserClassroomRole
                    = userClassroomRoleRepo
                    .findByUser_UserIdAndClassroom_ClassroomId(user.getUserId(), classroomId);
            if(!optionalUserClassroomRole.isPresent()){
                return false;
            }
            UserClassroomRole userClassroomRole = optionalUserClassroomRole.get();
            if(userClassroomRole.getRole() != Role.Student && userClassroomRole.getRole() != Role.CR){
                return false;
            }
            attendance.setUser(user);
            user.getAttendances().add(attendance);
            attendance.setSession(session);
            session.getAttendances().add(attendance);
            /*
            System.out.println("code : "+attendance.getCode().equals(session.getCode()));
            System.out.println("time : "+checkWithinTime(session.getCreationTime(),session.getMinutes()));
            System.out.println("inside : "+checkInside(attendance.getCoordinate(),session.getBounds()));
            */
            if(attendance.getCode().equals(session.getCode())
                    && checkWithinTime(session.getCreationTime(),session.getMinutes())
                    && checkInside(attendance.getCoordinate(),session.getBounds())){
                attendance.setState(State.AutoAccepted);
            }else{
                attendance.setState(State.AutoRejected);
            }
            attendanceRepo.save(attendance);
            return true;
        }
        return false;
    }

    public Boolean checkWithinTime(Date attendanceTime, Long minutes){
        Date maxTime = addMinutesToDate(attendanceTime, Math.toIntExact(minutes));
        Date currentTime = new Date();
        return currentTime.before(maxTime);
    }

    public static Date addMinutesToDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }


    public Boolean verify(Long sessionId,Long attendanceId,State state) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalCurrentUser = userRepo.findByUsername(userPrincipal.getUsername());
            if (!optionalCurrentUser.isPresent()) {
                return false;
            }
            User currentUser = optionalCurrentUser.get();
            Optional<Session> optionalSession = sessionRepo.findById(sessionId);
            if(!optionalSession.isPresent()){
                return false;
            }
            Session session = optionalSession.get();
            Classroom classroom = session.getClassroom();
            Long classroomId = classroom.getClassroomId();
            Optional<UserClassroomRole> optionalCurrentUserClassroomRole
                    = userClassroomRoleRepo
                    .findByUser_UserIdAndClassroom_ClassroomId(currentUser.getUserId(), classroomId);
            if(!optionalCurrentUserClassroomRole.isPresent()){
                return false;
            }
            UserClassroomRole currentUserClassroomRole = optionalCurrentUserClassroomRole.get();
            if(currentUserClassroomRole.getRole() != Role.Teacher){
                return false;
            }
            Optional<Attendance> optionalAttendance = attendanceRepo.findById(attendanceId);
            if(!optionalAttendance.isPresent()){
                return false;
            }
            Attendance attendance = optionalAttendance.get();
            Optional<UserClassroomRole> optionalUserClassroomRole
                    = userClassroomRoleRepo
                    .findByUser_UserIdAndClassroom_ClassroomId(attendance.getUser().getUserId(),classroomId);
            if(!optionalUserClassroomRole.isPresent()){
                return false;
            }
            UserClassroomRole userClassroomRole = optionalUserClassroomRole.get();
            if(userClassroomRole.getRole() != Role.Student && userClassroomRole.getRole() != Role.CR){
                return false;
            }
            attendance.setState(state);
            attendanceRepo.save(attendance);
            return true;
        }
        return false;
    }



    public List<AttendanceDto> all(Long sessionId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(userPrincipal.getUsername());
            if(!optionalUser.isPresent()){
                return new ArrayList<>();
            }
            User user = optionalUser.get();
            Optional<Session> optionalSession = sessionRepo.findById(sessionId);
            if(!optionalSession.isPresent()){
                return new ArrayList<>();
            }
            Session session = optionalSession.get();
            Long classroomId = session.getClassroom().getClassroomId();
            Optional<UserClassroomRole> optionalUserClassroomRole
                    = userClassroomRoleRepo
                    .findByUser_UserIdAndClassroom_ClassroomId(user.getUserId(), classroomId);
            if(!optionalUserClassroomRole.isPresent()){
                return new ArrayList<>();
            }
            UserClassroomRole userClassroomRole = optionalUserClassroomRole.get();
            if(userClassroomRole.getRole() == Role.Teacher){
                return attendanceRepo.findBySessionIdSuper(sessionId);
            }else{
                return attendanceRepo.findBySessionId(sessionId);
            }
        }
        return new ArrayList<>();
    }

    private Boolean checkInside(Coordinate coordinate, List<Coordinate> bounds){
        /*
        System.out.println("coordinate "+coordinate);
        for(Coordinate bound:bounds){
            System.out.println("c "+bound);
        }
        */
        int amplifier = 1000000;
        Polygon polygon = new Polygon();
        polygon.npoints = bounds.size();
        int[] xPoints = new int[bounds.size()];
        int[] yPoints = new int[bounds.size()];
        int i = 0;
        for(Coordinate bound : bounds){
            xPoints[i] = (int) (bound.getLat() * amplifier);
            yPoints[i] = (int) (bound.getLon() * amplifier);
            ++i;
        }
        polygon.xpoints = xPoints;
        polygon.ypoints = yPoints;
        int lat = (int) Math.round(amplifier*coordinate.getLat());
        int lon = (int) Math.round(amplifier*coordinate.getLon());
        return checkInside(lat,lon,polygon);
    }

    private Boolean checkInside(int lat,int lon,Polygon boundaryPolygon){
        return boundaryPolygon.contains(lat, lon)
                || onVertex(boundaryPolygon, lat, lon)
                || mirror(boundaryPolygon).contains(-lat, -lon);
    }

    private Polygon mirror(Polygon p) {
        int nPoints = p.npoints;
        int[] xPoints = new int[nPoints];
        int[] yPoints = new int[nPoints];
        for (int i = 0; i < nPoints; ++i) {
            xPoints[i] = -p.xpoints[i];
            yPoints[i] = -p.ypoints[i];
        }
        return new Polygon(xPoints, yPoints, nPoints);
    }

    boolean onVertex(Polygon p, double x, double y) {
        int nPoints = p.npoints;
        for (int i = 0; i < nPoints; ++i)
            if (p.xpoints[i] == x && p.ypoints[i] == y)
                return true;
        return false;
    }
}
