package com.rm.repository;

import com.rm.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BillRepository
        extends JpaRepository<Bill, Long> {

    // =========================
    // BASIC QUERIES
    // =========================

    List<Bill> findByCreatedBy(String email);

    List<Bill> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    Optional<Bill> findByInvoiceNumber(
            String invoiceNumber
    );

    // =========================
    // TODAY SALES ANALYTICS
    // =========================

    @Query("""
            SELECT COALESCE(SUM(b.totalAmount),0)
            FROM Bill b
            WHERE b.createdAt BETWEEN :start AND :end
            AND b.paymentStatus = com.rm.entity.PaymentStatus.PAID
            """)
    BigDecimal getTodayRevenue(
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
            SELECT COUNT(b)
            FROM Bill b
            WHERE b.createdAt BETWEEN :start AND :end
            AND b.paymentStatus = com.rm.entity.PaymentStatus.PAID
            """)
    Long getTodayBillCount(
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
            SELECT COALESCE(SUM(b.totalItems),0)
            FROM Bill b
            WHERE b.createdAt BETWEEN :start AND :end
            AND b.paymentStatus = com.rm.entity.PaymentStatus.PAID
            """)
    Integer getTodayItemsSold(
            LocalDateTime start,
            LocalDateTime end
    );

    // =========================
    // TOP CUSTOMERS
    // =========================

    @Query("""
            SELECT b.customer.email,
                   SUM(b.totalAmount)
            FROM Bill b
            GROUP BY b.customer.email
            ORDER BY SUM(b.totalAmount) DESC
            """)
    List<Object[]> topCustomers();

    // =========================
    // MONTHLY REVENUE
    // =========================

    @Query("""
            SELECT COALESCE(SUM(b.totalAmount),0)
            FROM Bill b
            WHERE b.createdAt BETWEEN :start AND :end
            AND b.paymentStatus = com.rm.entity.PaymentStatus.PAID
            """)
    BigDecimal getRevenueBetweenDates(
            LocalDateTime start,
            LocalDateTime end
    );

    // =========================
    // TOTAL BILLS BETWEEN DATES
    // =========================

    @Query("""
            SELECT COUNT(b)
            FROM Bill b
            WHERE b.createdAt BETWEEN :start AND :end
            AND b.paymentStatus = com.rm.entity.PaymentStatus.PAID
            """)
    Long getBillCountBetweenDates(
            LocalDateTime start,
            LocalDateTime end
    );

    // =========================
    // TOTAL ITEMS BETWEEN DATES
    // =========================

    @Query("""
            SELECT COALESCE(SUM(b.totalItems),0)
            FROM Bill b
            WHERE b.createdAt BETWEEN :start AND :end
            AND b.paymentStatus = com.rm.entity.PaymentStatus.PAID
            """)
    Integer getItemsSoldBetweenDates(
            LocalDateTime start,
            LocalDateTime end
    );
}