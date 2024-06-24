package com.example.easyplan.controller;

import com.example.easyplan.domain.dto.ResponseScheduleDTO;
import com.example.easyplan.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedule")
    public String createSchedule(@RequestBody ResponseScheduleDTO responseScheduleDTO){
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("schedule_id: " + responseScheduleDTO.getId());
        scheduleService.registSchedule(Long.parseLong(responseScheduleDTO.getId()), userId, responseScheduleDTO.getDate());
        return "ok";
    }

    @DeleteMapping("/schedule/{idd}")
    public String deleteSchedule(@PathVariable("idd") String idd){
        log.info("schedule_id: " + Long.parseLong(idd));
        scheduleService.deleteSchedule(Long.parseLong(idd));
        return "ok";
    }
}