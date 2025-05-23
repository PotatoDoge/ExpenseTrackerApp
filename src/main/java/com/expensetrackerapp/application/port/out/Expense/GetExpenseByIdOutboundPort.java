package com.expensetrackerapp.application.port.out.Expense;

import java.util.Optional;

public interface GetExpenseByIdOutboundPort <T> {
    Optional<T> getExpenseById(Long id);
}
