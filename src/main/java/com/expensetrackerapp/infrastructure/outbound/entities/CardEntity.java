package com.expensetrackerapp.infrastructure.outbound.entities;

import com.expensetrackerapp.domain.enums.CardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private CardType type;
    private String lastDigits;
    private String bankName;

}
