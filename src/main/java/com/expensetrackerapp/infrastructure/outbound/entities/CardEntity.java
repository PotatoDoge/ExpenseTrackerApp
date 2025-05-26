package com.expensetrackerapp.infrastructure.outbound.entities;

import com.expensetrackerapp.domain.enums.CardType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "expenses")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private CardType type;
    private String lastDigits;
    private String bankName;

    @OneToMany(mappedBy = "card")
    private List<ExpenseEntity> expenses;

}
