package com.bloodconnect.controller;

import com.bloodconnect.dto.BloodRequestDto;
import com.bloodconnect.service.BloodRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class BloodRequestController {

    @Autowired
    private BloodRequestService service;

    @PostMapping
    public ResponseEntity<BloodRequestDto> createRequest(@RequestBody BloodRequestDto dto) {
        return ResponseEntity.ok(service.createRequest(dto));
    }

    @GetMapping
    public ResponseEntity<List<BloodRequestDto>> getRequests(
            @RequestParam(required = false) String group,
            @RequestParam(required = false) String city) {
        return ResponseEntity.ok(service.getRequests(group, city));
    }
}
