package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.GetExpenses.GetExpensesUseCase;
import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseUseCase;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.shared.CustomResponse;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/expenses")
@AllArgsConstructor
@Log4j2
public class ExpenseController {

    private final SaveExpenseUseCase<ExpenseDTO> saveExpenseUseCase;
    private final GetExpensesUseCase<ExpenseDTO> getExpensesUseCase;

    @PostMapping
    public ResponseEntity<CustomResponse> saveExpense(@RequestBody SaveExpenseRequest request) {
        log.info("Received request to save expense: name={}, amount={}, category={}",
                request.getName(),
                request.getAmount(),
                request.getCategory());
        ExpenseDTO savedExpense = saveExpenseUseCase.saveExpense(request);
        CustomResponse response = CustomResponse
                .builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Expense saved successfully")
                .data(Map.of("expense", savedExpense)).build();
        log.info("Expense saved successfully with ID: {}", savedExpense.getId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<CustomResponse> getExpenses() {
        log.info("Received request to get all expenses");
        List<ExpenseDTO> expenses = getExpensesUseCase.getExpenses();
        CustomResponse response = CustomResponse
                .builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Expenses fetched successfully")
                .data(Map.of("expenses", expenses)).build();
        return ResponseEntity.status(200).body(response);
    }
}