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
- If `isPaidInFull = true`, `installments` should be **null** or **0**.
- If `isPaidInFull = false`, `installments` must be **> 1**.
- Later versions could calculate monthly installment amounts automatically (amount ÷ installments).