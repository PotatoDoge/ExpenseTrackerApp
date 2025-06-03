## User Stories

### 3.1 As a user, I want to specify whether an expense was paid in full or in installments
- So that I can distinguish immediate payments from deferred ones.

### 3.2 As a user, I want to enter the number of installments for an expense
- So that I can properly track payments spread over time.

### 3.3 As a user, I want to view whether an expense was paid in full or has pending installments
- So that I can manage my future financial obligations more easily.

### 3.4 As a user, I want to edit the installment information of an expense
- So that I can fix errors or reflect changes in my payment plans.

---

# 📌 Notes
- There are **two types of recurring expenses**:
    1. **Installment-based (fixed-term)**:
        - `isRecurring = false`
        - `installments > 1`
        - A new expense is generated monthly until `installments` are completed.
        - `parentExpenseId` can help track which expense each installment belongs to.
        - `nextDueDate` can be used to determine when to generate the next installment.

    2. **Open-ended recurring (indefinite)**:
        - `isRecurring = true`
        - `installments = null` or `1`
        - `recurrenceType` (e.g., MONTHLY) determines the frequency.
        - A new expense is generated indefinitely based on `expenseDate`'s day of month.
        - The user must manually stop or cancel it.

- A daily scheduled job should:
    - Check if today matches the `expenseDate`'s day and recurrence type.
    - Generate a new expense entry if it's time.
    - For installment expenses, track how many have been created and stop when done.
