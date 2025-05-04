package com.example.backend.service;

import com.example.backend.dto.OrderResponseDTO;
import com.example.backend.dto.OrderItemDTO;
import com.example.backend.models.Order;
import com.example.backend.models.OrderDetail;
import com.example.backend.models.User;
import com.example.backend.models.Address;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AddressRepository addressRepository; // Thêm AddressRepository

    /**
     * Lấy danh sách tất cả đơn hàng
     */
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Lấy danh sách đơn hàng theo User ID
     */
    public List<OrderResponseDTO> getOrdersByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserUid(userId);
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Lấy đơn hàng theo ID
     */
    public Optional<OrderResponseDTO> getOrderById(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        return orderOpt.map(this::convertToDTO);
    }

    /**
     * Tạo đơn hàng mới
     */
    @Transactional
    public Order createOrder(Order order) {
        if (order.getUser() == null || userRepository.findById(order.getUser().getUid()).isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        return orderRepository.save(order);
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    @Transactional
    public Order updateOrder(Long id, String status) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(status);
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
    }

    /**
     * Hủy đơn hàng
     */
    @Transactional
    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }

    /**
     * Chuyển đổi từ Order Entity sang OrderResponseDTO
     */
    private OrderResponseDTO convertToDTO(Order order) {
        User user = order.getUser();
        String customerName = (user != null) ? user.getDisplayName() : "Unknown";
        String phone = (user != null) ? user.getPhone() : "Unknown";
        
        // Tìm địa chỉ mặc định của người dùng trong bảng address
        String address = "Unknown";
        if (user != null) {
            Optional<Address> addressOpt = addressRepository.findByUserUidAndIsDefault(user.getUid(), true);
            address = addressOpt.map(a -> a.getHouseNumber() + ", " + a.getStreet() + ", " + a.getWard() + ", " + a.getDistrict() + ", " + a.getCity()).orElse("Unknown");
        }

        List<OrderItemDTO> items = (order.getOrderDetails() != null)
                ? order.getOrderDetails().stream().map(this::convertOrderDetailToDTO).collect(Collectors.toList())
                : List.of();

        return new OrderResponseDTO(
                order.getOrderId(),
                customerName,
                phone,
                address,
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getPaymentMethod(),
                items
        );
    }

    /**
     * Chuyển đổi từ OrderDetail Entity sang OrderItemDTO
     */
    private OrderItemDTO convertOrderDetailToDTO(OrderDetail orderDetail) {
        String productName = (orderDetail.getProduct() != null) ? orderDetail.getProduct().getName() : "Unknown";
        return new OrderItemDTO(
                productName,
                orderDetail.getQuantity(),
                orderDetail.getPrice()
        );
    }
}
