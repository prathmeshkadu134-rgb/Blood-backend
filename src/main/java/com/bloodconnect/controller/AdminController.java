package com.bloodconnect.controller;

import com.bloodconnect.entity.BloodRequest;
import com.bloodconnect.entity.Transaction;
import com.bloodconnect.repository.BloodRequestRepository;
import com.bloodconnect.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/requests")
    public ResponseEntity<List<BloodRequest>> getAllBloodRequests() {
        return ResponseEntity.ok(bloodRequestRepository.findAll());
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }
}
