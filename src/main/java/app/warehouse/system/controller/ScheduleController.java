package app.warehouse.system.controller;

import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping(value = "/schedule")
    public MessageHandler scheduleOrder(
            @RequestParam(name = "orderId") Long orderId,
            @RequestParam(name = "date") String date) {
        return scheduleService.scheduleOrder(orderId, date);
    }
}
