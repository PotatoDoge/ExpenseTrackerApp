package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseUseCase;
import com.expensetrackerapp.dto.ExpenseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
@AllArgsConstructor
@Log4j2
public class ExpenseController {

    private final SaveExpenseUseCase<ExpenseDTO> saveExpenseUseCase;

    @PostMapping
    public ResponseEntity<ExpenseDTO> saveExpense(@RequestBody SaveExpenseRequest request) {
        log.info("Received request to save expense: {}", request);
        return ResponseEntity.status(201).body(saveExpenseUseCase.saveExpense(request));
    }
}