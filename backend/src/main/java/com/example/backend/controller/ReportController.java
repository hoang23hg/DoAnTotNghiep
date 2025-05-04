package com.example.backend.controller;

import com.example.backend.dto.ProductSalesDTO;
import com.example.backend.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 📌 Báo cáo doanh số theo ngày (mặc định hôm nay nếu không truyền tham số)
    @GetMapping("/sales/daily")
    public List<ProductSalesDTO> getDailySales(@RequestParam(required = false) String date) {
        LocalDate parsedDate = null;
        if (date != null) {
            try {
                parsedDate = LocalDate.parse(date);  // Chuyển chuỗi ngày sang LocalDate
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Ngày không hợp lệ, định dạng đúng là yyyy-MM-dd");
            }
        }
        return reportService.getDailyProductSales(parsedDate);
    }

    // 📌 Báo cáo doanh số theo tháng (mặc định tháng hiện tại nếu không truyền tham số)
    @GetMapping("/sales/monthly")
    public List<ProductSalesDTO> getMonthlySales(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return reportService.getMonthlyProductSales(month, year);
    }

    // 📌 Báo cáo doanh số theo năm (mặc định năm hiện tại nếu không truyền tham số)
    @GetMapping("/sales/yearly")
    public List<ProductSalesDTO> getYearlySales(@RequestParam(required = false) Integer year) {
        return reportService.getYearlyProductSales(year);
    }
}
