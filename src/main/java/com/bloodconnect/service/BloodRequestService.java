package com.bloodconnect.service;

import com.bloodconnect.dto.BloodRequestDto;
import com.bloodconnect.entity.BloodRequest;
import com.bloodconnect.entity.User;
import com.bloodconnect.repository.BloodRequestRepository;
import com.bloodconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BloodRequestService {

    @Autowired
    private BloodRequestRepository repository;
    
    @Autowired
    private UserRepository userRepository;

    public BloodRequestDto createRequest(BloodRequestDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BloodRequest request = BloodRequest.builder()
                .requester(requester)
                .patientName(dto.getPatientName())
                .bloodGroup(dto.getBloodGroup())
                .city(dto.getCity())
                .hospitalName(dto.getHospitalName())
                .status("ACTIVE")
                .build();

        BloodRequest saved = repository.save(request);
        return mapToDto(saved);
    }

    public List<BloodRequestDto> getRequests(String group, String city) {
        List<BloodRequest> requests;
        if (group != null && city != null) {
            requests = repository.findByBloodGroupAndCityAndStatus(group, city, "ACTIVE");
        } else {
            requests = repository.findByStatus("ACTIVE");
        }
        return requests.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private BloodRequestDto mapToDto(BloodRequest request) {
        return BloodRequestDto.builder()
                .id(request.getId())
                .patientName(request.getPatientName())
                .bloodGroup(request.getBloodGroup())
                .city(request.getCity())
                .hospitalName(request.getHospitalName())
                .status(request.getStatus())
                .requesterEmail(request.getRequester().getEmail())
                .build();
    }
}
