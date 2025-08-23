package com.apiot.mediflow.appointment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> getAppointments() {
        return ResponseEntity.ok(appointmentService.getAppointments());
    }
}
