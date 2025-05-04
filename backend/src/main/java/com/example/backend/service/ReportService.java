package com.example.backend.service;

import com.example.backend.dto.ProductSalesDTO;
import com.example.backend.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 📌 Báo cáo doanh số theo ngày (nếu `date` null → lấy ngày hiện tại)
    public List<ProductSalesDTO> getDailyProductSales(LocalDate date) {
        if (date != null) {
            return reportRepository.getDailyProductSalesByDate(date);
        }
        return reportRepository.getDailyProductSales();
    }

    // 📌 Báo cáo doanh số theo tháng (nếu `month` và `year` null → lấy tháng hiện tại)
    public List<ProductSalesDTO> getMonthlyProductSales(Integer month, Integer year) {
        if (month != null && year != null) {
            return reportRepository.getMonthlyProductSalesByMonthYear(month, year);
        }
        return reportRepository.getMonthlyProductSales();
    }

    // 📌 Báo cáo doanh số theo năm (nếu `year` null → lấy năm hiện tại)
    public List<ProductSalesDTO> getYearlyProductSales(Integer year) {
        if (year != null) {
            return reportRepository.getYearlyProductSalesByYear(year);
        }
        return reportRepository.getYearlyProductSales();
    }
}
