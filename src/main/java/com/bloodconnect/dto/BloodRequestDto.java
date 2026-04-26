package com.bloodconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BloodRequestDto {
    private Long id;
    private String patientName;
    private String bloodGroup;
    private String city;
    private String hospitalName;
    private String status;
    private String requesterEmail; // Hide full user details, just returning email
}
