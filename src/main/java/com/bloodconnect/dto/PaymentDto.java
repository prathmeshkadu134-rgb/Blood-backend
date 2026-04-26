package com.bloodconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PaymentDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderRequest {
        private Double amount;
        private String currency; // e.g., "USD" or "INR"
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderResponse {
        private String orderId;
        private Double amount;
        private String currency;
        private String status;
    }
}
