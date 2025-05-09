package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.Expense.DeleteExpense.DeleteExpenseUseCase;
import com.expensetrackerapp.application.port.in.Expense.GetExpenses.GetExpensesFilters;
import com.expensetrackerapp.application.port.in.Expense.GetExpenses.GetExpensesUseCase;
import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseUseCase;
import com.expensetrackerapp.application.port.in.Expense.UpdateExpense.UpdateExpenseRequest;
import com.expensetrackerapp.application.port.in.Expense.UpdateExpense.UpdateExpenseUseCase;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.shared.CustomResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private final GetExpensesUseCase<ExpenseDTO, GetExpensesFilters> getExpensesUseCase;
    private final UpdateExpenseUseCase<ExpenseDTO> updateExpenseUseCase;
    private final DeleteExpenseUseCase deleteExpenseUseCase;

    @PostMapping
    public ResponseEntity<CustomResponse> saveExpense(@RequestBody SaveExpenseRequest request) {
        log.info("Received request to save expense: name={}, amount={}, category={}",
                request.getName(),
                request.getAmount(),
                request.getCategoryId());
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
    public ResponseEntity<CustomResponse> getExpenses(
            @RequestParam(required = false) Long expenseId,
            @RequestParam(required = false) String expenseName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) BigDecimal amount,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) LocalDate expenseDate,
            @RequestParam(required = false) Boolean requiresInvoice,
            @RequestParam(required = false) Integer installments,
            @RequestParam(required = false) String vendor,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean paidInFull,
            @RequestParam(required = false) Boolean recurring
    ) {
        GetExpensesFilters filter = GetExpensesFilters.builder()
                .expenseId(expenseId)
                .expenseName(expenseName)
                .description(description)
                .amount(amount)
                .currency(currency)
                .expenseDate(expenseDate)
                .requiresInvoice(requiresInvoice)
                .installments(installments)
                .vendor(vendor)
                .location(location)
                .paidInFull(paidInFull)
                .recurring(recurring)
                .build();
        log.info("Received request to get all expenses with the following filters: {}", filter);
        List<ExpenseDTO> expenses = getExpensesUseCase.getExpenses(filter);
        CustomResponse response = CustomResponse
                .builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Expenses fetched successfully")
                .data(Map.of("expenses", expenses)).build();
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("{expenseId}")
    public ResponseEntity<CustomResponse> updateExpense(@RequestBody UpdateExpenseRequest request, @PathVariable Long expenseId) {
        log.info("Received request to update expense: name={}, amount={}, category={}",
                request.getName(),
                request.getAmount(),
                request.getCategoryId());
        ExpenseDTO updatedExpense = updateExpenseUseCase.updateExpense(request, expenseId);
        CustomResponse response = CustomResponse
                .builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Expense updated successfully")
                .data(Map.of("expense", updatedExpense)).build();
        log.info("Expense updated successfully with ID: {}", updatedExpense.getId());
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("{expenseId}")
    public ResponseEntity<CustomResponse> deleteExpense(@PathVariable Long expenseId) {
        log.info("Received request to delete expense with ID: {}", expenseId);
        deleteExpenseUseCase.deleteExpense(expenseId);
        CustomResponse response = CustomResponse
                .builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Expense deleted successfully")
                .build();
        log.info("Expense deleted successfully with ID: {}", expenseId);
        return ResponseEntity.status(204).body(response);
    }

}