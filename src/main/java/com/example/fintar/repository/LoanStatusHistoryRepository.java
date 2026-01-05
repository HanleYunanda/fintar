package com.example.fintar.repository;

import com.example.fintar.entity.Loan;
import com.example.fintar.entity.LoanStatusHistory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanStatusHistoryRepository extends JpaRepository<LoanStatusHistory, UUID> {
  Optional<LoanStatusHistory> findFirstByLoanOrderByPerformedAtDesc(Loan loan);

  List<LoanStatusHistory> findByLoanId(UUID loanId);
}
