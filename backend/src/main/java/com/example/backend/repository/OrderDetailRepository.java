package com.example.backend.repository;

import com.example.backend.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>
 {
    // Lấy danh sách chi tiết đơn hàng theo ID đơn hàng
    List<OrderDetail> findByOrderOrderId(Long orderId);
}
