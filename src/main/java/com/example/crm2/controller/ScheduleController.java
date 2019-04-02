package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.ScheduleRequest;
import com.example.crm2.exception.AppException;
import com.example.crm2.model.timetable.Schedule;
import com.example.crm2.model.user.User;
import com.example.crm2.repo.ScheduleRepo;
import com.example.crm2.repo.UserRepo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleRepo scheduleRepo;

    @Autowired
    UserRepo userRepo;

    @GetMapping
    public Iterable<Schedule> getSchedules() {

        return scheduleRepo.findAll();
    }

    @GetMapping("/{id}")
    public Schedule getScheduleById(@PathVariable Integer id) {

        Schedule schedule = scheduleRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Schedule with id: " + id + " not found"));

        return schedule;
    }

    @GetMapping("/teacher/{id}")
    public Schedule teacherSchedules(@PathVariable Integer id) {

        Schedule schedule = scheduleRepo.findAllByAuthor_Id(id).orElseThrow(() ->
                new IllegalArgumentException("Schedules with author id: " + id + " not found"));

        return schedule;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody ScheduleRequest request, Principal principal) {

        String name = principal.getName();

        User user = userRepo.findByUsername(name).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email: " + name));

        Schedule schedule = new Schedule();

        schedule.setDescription(request.getDescription());
        schedule.setDay(request.getDay());
        schedule.setTime(request.getTime());
        schedule.setRoom(request.getRoom());
        schedule.setAuthor(user);
        schedule.getShareUsers().addAll(identifyUser(request));

        scheduleRepo.save(schedule);

        return ResponseEntity.ok(new ApiResponse(true, "Schedule created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @PathVariable("id") Schedule scheduleFromDB,
            @RequestBody ScheduleRequest request
    ) {

        if (scheduleFromDB != null) {
            scheduleFromDB.setDescription(request.getDescription());
            scheduleFromDB.setDay(request.getDay());
            scheduleFromDB.setTime(request.getTime());
            scheduleFromDB.setRoom(request.getRoom());
            scheduleFromDB.getShareUsers().clear();
            scheduleFromDB.getShareUsers().addAll(identifyUser(request));
            scheduleRepo.save(scheduleFromDB);

            return ResponseEntity.ok(new ApiResponse(true, "Schedule updated successfully"));
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Schedule doesn't exist"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Schedule schedule) {

        if (schedule != null){
            scheduleRepo.delete(schedule);
            return ResponseEntity.ok(new ApiResponse(true, "schedule deleted successfully"));
        } else return new ResponseEntity<>(new ApiResponse(false, "This schedule doesn't exist"),
                HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "returns list of schedules from Users which add my id to the Share list")
    @GetMapping("/{id}/share")
    public List<Schedule> shareSchedule(@PathVariable Integer id) {

        return scheduleRepo.listOfScheduleSharedWithMe(id);
    }

    private Set<User> identifyUser(ScheduleRequest request) {

        Set<User> users = new HashSet<>();

        for (Integer n: request.getShareUsers()) {
            users.add(userRepo.findById(n).orElseThrow(
                    () -> new AppException("No user with id: " + n)));
        }
        return users;
    }
}
