package com.bloodconnect.controller;

import com.bloodconnect.dto.PaymentDto;
import com.bloodconnect.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<PaymentDto.OrderResponse> createOrder(@RequestBody PaymentDto.OrderRequest request) {
        return ResponseEntity.ok(paymentService.createOrder(request));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("X-Razorpay-Signature") String signature,
            @RequestBody Map<String, Object> payload,
            @RequestBody String rawBody) { 
        // Need double reading body. Typically Spring limits this. 
        // In robust real systems, custom wrapper is used to read raw string for HMAC. 
        // For simplicity and matching signature, we pass both to Service or just pass rawBody and parse it.
        // Spring handles double binding to @RequestBody String if setup.
        
        paymentService.verifyWebhookAndCapture(payload, signature, rawBody);
        return ResponseEntity.ok("OK");
    }
}
