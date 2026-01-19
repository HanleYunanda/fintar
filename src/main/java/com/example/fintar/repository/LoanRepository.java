package com.example.fintar.repository;

import com.example.fintar.entity.Loan;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import com.example.fintar.dto.DisbursementTrendDTO;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {

    @Modifying
    @Query("UPDATE Loan l SET l.createdAt = :createdAt, l.createdBy = :createdBy WHERE l.id = :id")
    void updateCreatedAtAndCreatedBy(@Param("id") UUID id,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("createdBy") UUID createdBy);

    List<Loan> findByCreatedBy(UUID createdBy);

    // Dashboard Queries
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.createdAt BETWEEN :start AND :end")
    Long countApplicationsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(l.outstandingDebt), 0) FROM Loan l WHERE l.status = 'DISBURSED' AND (l.createdAt BETWEEN :start AND :end)")
    Long sumTotalOutstandingActive(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.status = 'DISBURSED' AND (l.createdAt BETWEEN :start AND :end)")
    Long countActiveLoans(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.status = 'APPROVED' AND l.createdAt BETWEEN :start AND :end")
    Long countApprovedApplicationsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
            SELECT new com.example.fintar.dto.DisbursementTrendDTO(
                FUNCTION('YEAR', l.createdAt),
                FUNCTION('MONTH', l.createdAt),
                SUM(l.principalDebt)
            )
            FROM Loan l
            WHERE l.createdAt >= :startDate
            AND l.status = 'DISBURSED'
            AND l.createdAt <= :endDate
            GROUP BY
                FUNCTION('YEAR', l.createdAt),
                FUNCTION('MONTH', l.createdAt)
            ORDER BY
                FUNCTION('YEAR', l.createdAt),
                FUNCTION('MONTH', l.createdAt)
            """)
    List<DisbursementTrendDTO> findDisbursementTrendsAfter(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT new com.example.fintar.dto.ApplicationStatusDTO(
                CAST(l.status AS string),
                COUNT(l)
            )
            FROM Loan l
            GROUP BY l.status
            """)
    List<com.example.fintar.dto.ApplicationStatusDTO> countApplicationsByStatus();

    @Query("""
            SELECT new com.example.fintar.dto.BestSellingProductDTO(
                CONCAT(p.plafond.name, ' ', p.tenor),
                COUNT(l)
            )
            FROM Loan l
            JOIN l.product p
            GROUP BY p.plafond.name, p.tenor
            ORDER BY COUNT(l) DESC
            LIMIT :limit
            """)
    List<com.example.fintar.dto.BestSellingProductDTO> findBestSellingProducts(@Param("limit") Integer limit);

}
