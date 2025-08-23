package com.apiot.mediflow.appointment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(@RequestBody AppointmentRequestDto appointmentRequestDto) {
        AppointmentResponseDto created = appointmentService.createAppointment(appointmentRequestDto);
        return ResponseEntity
                .created(URI.create("/api/appointments/" + created.getId()))
                .body(created);
    }
}
