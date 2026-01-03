package com.example.fintar.repository;

import com.example.fintar.entity.Loan;
import com.example.fintar.entity.LoanStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoanStatusHistoryRepository extends JpaRepository<LoanStatusHistory, UUID> {
    Optional<LoanStatusHistory> findFirstByLoanOrderByPerformedAtDesc(Loan loan);

    List<LoanStatusHistory> findByLoanId(UUID loanId);
}
