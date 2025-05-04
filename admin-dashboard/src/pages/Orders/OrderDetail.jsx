import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";

const OrderDetail = () => {
    const { id } = useParams(); // Lấy orderId từ URL
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch(`http://localhost:8080/api/orders/${id}`)
            .then((res) => res.json())
            .then((data) => {
                setOrder(data);
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setLoading(false);
            });
    }, [id]);

    if (loading) return <p className="text-center text-lg">Đang tải thông tin đơn hàng...</p>;
    if (error) return <p className="text-center text-red-500 text-lg">Lỗi: {error}</p>;
    if (!order) return <p className="text-center text-lg">Không tìm thấy đơn hàng</p>;

    return (
        <div className="p-6 bg-gray-100 min-h-screen">
            {/* Navbar */}
            <div className="bg-white shadow-md rounded-lg p-4 flex justify-between items-center mb-6">
                <h2 className="text-2xl font-semibold text-gray-800">Chi tiết đơn hàng #{order.orderId}</h2>
                <Link to="/orders" className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition">
                    Quay lại danh sách đơn hàng
                </Link>
            </div>

            {/* Thông tin đơn hàng */}
            <div className="bg-white shadow-md rounded-lg p-6 mb-6">
                <p><strong>Tên khách hàng:</strong> {order.customerName || "Không có thông tin"}</p>
                <p><strong>Số điện thoại:</strong> {order.phone || "Không có thông tin"}</p>
                <p><strong>Địa chỉ:</strong> {order.address || "Không có thông tin"}</p>
                <p><strong>Ngày đặt hàng:</strong> {order.orderDate ? new Date(order.orderDate).toLocaleDateString("vi-VN") : "Không xác định"}</p>
                <p><strong>Phương thức thanh toán:</strong> {order.paymentMethod || "Không xác định"}</p>
                <p><strong>Tổng tiền:</strong>$ {order.totalPrice.toLocaleString("vi-VN")}</p>
                <p>
                    <strong>Trạng thái:</strong>
                    <span className={`px-2 py-1 rounded text-white ml-2 
                        ${order.status === "pending" ? "bg-yellow-500" : 
                          order.status === "completed" ? "bg-green-500" : 
                          "bg-red-500"}`}>
                        {order.status}
                    </span>
                </p>
            </div>

            {/* Danh sách sản phẩm */}
            <div className="bg-white shadow-md rounded-lg p-6">
                <h3 className="text-xl font-semibold mb-4">Sản phẩm trong đơn hàng</h3>
                <table className="w-full border-collapse">
                    <thead className="bg-gray-200">
                        <tr>
                            <th className="p-3 text-left">Tên sản phẩm</th>
                            <th className="p-3 text-center">Số lượng</th>
                            <th className="p-3 text-right">Giá</th>
                        </tr>
                    </thead>
                    <tbody>
                        {order.items.length > 0 ? order.items.map((item, index) => (
                            <tr key={index} className={index % 2 === 0 ? "bg-gray-50" : "bg-white"}>
                                <td className="p-3">{item.productName}</td>
                                <td className="p-3 text-center">{item.quantity}</td>
                                <td className="p-3 text-right">${item.price.toLocaleString("vi-VN")}</td>
                            </tr>
                        )) : (
                            <tr>
                                <td colSpan="3" className="p-3 text-center">Không có sản phẩm trong đơn hàng</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default OrderDetail;
