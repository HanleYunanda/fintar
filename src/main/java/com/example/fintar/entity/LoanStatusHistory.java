package com.example.fintar.entity;

import com.example.fintar.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loan_status_histories")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@SQLDelete(sql = "UPDATE loan_status_histories SET is_deleted = 1 WHERE id = ?")
//@SQLRestriction("is_deleted = 0")
public class LoanStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne
    private Loan loan;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus action;

    private String note;

    @ManyToOne
    private User performedBy;

    @Column(nullable = false)
    private LocalDateTime performedAt;

}
