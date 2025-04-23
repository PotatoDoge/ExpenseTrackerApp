package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseUseCase;
import com.expensetrackerapp.dto.ExpenseDTO;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class ExpenseControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private SaveExpenseUseCase<ExpenseDTO> saveExpenseUseCase;

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
                .currency(Currency.getInstance("USD"))
                .dateTime(LocalDateTime.parse("2024-04-21T10:30:00"))
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

        String saveExpenseRequestAsJSON = readJson("messages/SaveExpenseRequest.json");
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
}
