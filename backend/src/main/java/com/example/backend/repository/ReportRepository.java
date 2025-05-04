package com.example.backend.repository;

import com.example.backend.dto.ProductSalesDTO;
import com.example.backend.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<OrderDetail, Integer> {

    // 📌 Báo cáo doanh số theo ngày (mặc định là ngày hiện tại)
    @Query("SELECT new com.example.backend.dto.ProductSalesDTO(p.name, SUM(od.quantity), SUM(od.price * od.quantity)) " +
           "FROM OrderDetail od " +
           "JOIN od.product p " +
           "JOIN od.order o " +
           "WHERE FUNCTION('DATE', o.orderDate) = CURRENT_DATE " +
           "GROUP BY p.name")
    List<ProductSalesDTO> getDailyProductSales();

    // 📌 Báo cáo doanh số theo ngày cụ thể (truyền tham số ngày)
    @Query("SELECT new com.example.backend.dto.ProductSalesDTO(p.name, SUM(od.quantity), SUM(od.price * od.quantity)) " +
           "FROM OrderDetail od " +
           "JOIN od.product p " +
           "JOIN od.order o " +
           "WHERE FUNCTION('DATE', o.orderDate) = :date " +
           "GROUP BY p.name")
    List<ProductSalesDTO> getDailyProductSalesByDate(@Param("date") LocalDate date);

    // 📌 Báo cáo doanh số theo tháng hiện tại
    @Query("SELECT new com.example.backend.dto.ProductSalesDTO(p.name, SUM(od.quantity), SUM(od.price * od.quantity)) " +
           "FROM OrderDetail od " +
           "JOIN od.product p " +
           "JOIN od.order o " +
           "WHERE FUNCTION('MONTH', o.orderDate) = FUNCTION('MONTH', CURRENT_DATE) " +
           "AND FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE) " +
           "GROUP BY p.name")
    List<ProductSalesDTO> getMonthlyProductSales();

    // 📌 Báo cáo doanh số theo tháng và năm cụ thể
    @Query("SELECT new com.example.backend.dto.ProductSalesDTO(p.name, SUM(od.quantity), SUM(od.price * od.quantity)) " +
           "FROM OrderDetail od " +
           "JOIN od.product p " +
           "JOIN od.order o " +
           "WHERE FUNCTION('MONTH', o.orderDate) = :month " +
           "AND FUNCTION('YEAR', o.orderDate) = :year " +
           "GROUP BY p.name")
    List<ProductSalesDTO> getMonthlyProductSalesByMonthYear(@Param("month") Integer month, @Param("year") Integer year);

    // 📌 Báo cáo doanh số theo năm hiện tại
    @Query("SELECT new com.example.backend.dto.ProductSalesDTO(p.name, SUM(od.quantity), SUM(od.price * od.quantity)) " +
           "FROM OrderDetail od " +
           "JOIN od.product p " +
           "JOIN od.order o " +
           "WHERE FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE) " +
           "GROUP BY p.name")
    List<ProductSalesDTO> getYearlyProductSales();

    // 📌 Báo cáo doanh số theo năm cụ thể
    @Query("SELECT new com.example.backend.dto.ProductSalesDTO(p.name, SUM(od.quantity), SUM(od.price * od.quantity)) " +
           "FROM OrderDetail od " +
           "JOIN od.product p " +
           "JOIN od.order o " +
           "WHERE FUNCTION('YEAR', o.orderDate) = :year " +
           "GROUP BY p.name")
    List<ProductSalesDTO> getYearlyProductSalesByYear(@Param("year") Integer year);
}
