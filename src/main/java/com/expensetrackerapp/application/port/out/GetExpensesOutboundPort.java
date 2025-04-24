package com.expensetrackerapp.application.port.out;

import java.util.List;

public interface GetExpensesOutboundPort<T> {
    List<T> getExpenses();
}