package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.Expense.DeleteExpense.DeleteExpenseUseCase;
import com.expensetrackerapp.application.port.in.Expense.GetExpenses.GetExpensesFilters;
import com.expensetrackerapp.application.port.in.Expense.GetExpenses.GetExpensesUseCase;
import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseUseCase;
import com.expensetrackerapp.application.port.in.Expense.UpdateExpense.UpdateExpenseRequest;
import com.expensetrackerapp.application.port.in.Expense.UpdateExpense.UpdateExpenseUseCase;
import com.expensetrackerapp.dto.ExpenseDTO;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class ExpenseControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private SaveExpenseUseCase<ExpenseDTO> saveExpenseUseCase;

    @Mock
    private GetExpensesUseCase<ExpenseDTO, GetExpensesFilters> getExpensesUseCase;

    @Mock
    private UpdateExpenseUseCase<ExpenseDTO> updateExpenseUseCase;

    @Mock
    private DeleteExpenseUseCase deleteExpenseUseCase;

    @InjectMocks
    private ExpenseController expenseController;

    private ExpenseDTO expenseDTO;

    private String readJson(String path) throws IOException {
        return new String(
                Files.readAllBytes(Paths.get("src/test/resources/" + path))
        );
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
        expenseDTO = ExpenseDTO.builder()
                .id(1L)
                .name("Lunch")
                .description("Description of expense")
                .amount(BigDecimal.valueOf(50.0))
                .currency("USD")
                .expenseDate(LocalDate.now())
                .requiresInvoice(true)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(false)
                .vendor("Vendor A")
                .location("New York")
                .build();
    }

    @Test
    void testSaveExpenseSuccess() throws Exception {

        String saveExpenseRequestAsJSON = readJson("messages/Expense/SaveExpenseRequest.json");
        when(saveExpenseUseCase.saveExpense(any(SaveExpenseRequest.class)))
                .thenReturn(expenseDTO);

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saveExpenseRequestAsJSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Expense saved successfully"))
                .andExpect(jsonPath("$.data.expense.id").value("1"))
                .andExpect(jsonPath("$.data.expense.name").value("Lunch"))
                .andExpect(jsonPath("$.data.expense.amount").value(50.0));
    }

    @Test
    void testSaveExpenseWithNullRequestBody() {
        ServletException exception = assertThrows(ServletException.class, () ->
                mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest()));
        assertNotNull(exception);
    }

    @Test
    void testSaveExpenseWithInvalidData() throws Exception {
        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\", \"amount\":-10.0, \"category\":\"Food\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetExpensesSuccess() throws Exception {
        List<ExpenseDTO> expenses = List.of(expenseDTO);
        when(getExpensesUseCase.getExpenses(any(GetExpensesFilters.class)))
                .thenReturn(expenses);

        mockMvc.perform(get("/expenses")
                        .param("expenseName", "Lunch"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Expenses fetched successfully"))
                .andExpect(jsonPath("$.data.expenses[0].name").value("Lunch"))
                .andExpect(jsonPath("$.data.expenses[0].amount").value(50.0));
    }

    @Test
    void testUpdateExpenseSuccess() throws Exception {
        String updateExpenseRequestAsJSON = readJson("messages/Expense/UpdateExpenseRequest.json");

        ExpenseDTO updatedExpenseDTO = ExpenseDTO.builder()
                .id(1L)
                .name("Updated Lunch")
                .description("Updated description")
                .amount(BigDecimal.valueOf(60.0))
                .currency("USD")
                .expenseDate(LocalDate.now())
                .requiresInvoice(false)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(false)
                .vendor("Updated Vendor")
                .location("San Francisco")
                .build();

        when(updateExpenseUseCase.updateExpense(any(UpdateExpenseRequest.class), any(Long.class)))
                .thenReturn(updatedExpenseDTO);

        mockMvc.perform(put("/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateExpenseRequestAsJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Expense updated successfully"))
                .andExpect(jsonPath("$.data.expense.name").value("Updated Lunch"))
                .andExpect(jsonPath("$.data.expense.amount").value(60.0));
    }

    @Test
    void testDeleteExpense_Success() throws Exception {
        // Arrange
        doNothing().when(deleteExpenseUseCase).deleteExpense(1L);

        // Act & Assert
        mockMvc.perform(delete("/expenses/{expenseId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("Expense deleted successfully"));

        verify(deleteExpenseUseCase,Mockito.times(1)).deleteExpense(1L);
    }
}
