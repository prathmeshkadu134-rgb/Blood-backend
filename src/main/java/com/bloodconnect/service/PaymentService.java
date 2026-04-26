package com.bloodconnect.service;

import com.bloodconnect.dto.PaymentDto;
import com.bloodconnect.entity.Transaction;
import com.bloodconnect.repository.TransactionRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    @Autowired
    private TransactionRepository transactionRepository;

    public PaymentDto.OrderResponse createOrder(PaymentDto.OrderRequest request) {
        try {
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);
            
            JSONObject orderRequest = new JSONObject();
            // Razorpay accepts amount in subunits (e.g. paise for INR, cents for USD)
            orderRequest.put("amount", (int)(request.getAmount() * 100)); 
            orderRequest.put("currency", request.getCurrency());
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = razorpay.orders.create(orderRequest);
            String orderId = order.get("id");

            // Save to DB
            Transaction transaction = Transaction.builder()
                    .orderId(orderId)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status("PENDING")
                    .build();
            transactionRepository.save(transaction);

            return PaymentDto.OrderResponse.builder()
                    .orderId(orderId)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status("PENDING")
                    .build();
                    
        } catch (Exception e) {
            throw new RuntimeException("Could not create Razorpay order: " + e.getMessage());
        }
    }

    public void verifyWebhookAndCapture(Map<String, Object> payload, String signatureHeader, String rawBody) {
        try {
            // Verify signature
            boolean isValid = Utils.verifyWebhookSignature(rawBody, signatureHeader, webhookSecret);
            if (!isValid) {
                throw new RuntimeException("Invalid webhook signature");
            }

            // Extract payload
            Map<String, Object> event = (Map<String, Object>) payload;
            String eventType = (String) event.get("event");

            if ("payment.captured".equals(eventType)) {
                Map<String, Object> payloadObj = (Map<String, Object>) event.get("payload");
                Map<String, Object> paymentObj = (Map<String, Object>) payloadObj.get("payment");
                Map<String, Object> entity = (Map<String, Object>) paymentObj.get("entity");
                
                String orderId = (String) entity.get("order_id");
                String paymentId = (String) entity.get("id");

                Transaction transaction = transactionRepository.findByOrderId(orderId)
                        .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
                
                transaction.setPaymentId(paymentId);
                transaction.setSignature(signatureHeader);
                transaction.setStatus("SUCCESS");
                
                transactionRepository.save(transaction);
            }
        } catch (Exception e) {
            throw new RuntimeException("Webhook processing error: " + e.getMessage());
        }
    }
}
