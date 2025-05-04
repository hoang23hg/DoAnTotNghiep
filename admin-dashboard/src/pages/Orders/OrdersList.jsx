import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";

const OrderList = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch("http://localhost:8080/api/orders")
            .then((res) => res.json())
            .then((data) => {
                setOrders(data);
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    const handleStatusChange = (orderId, newStatus) => {
        fetch(`http://localhost:8080/api/orders/${orderId}/status`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ status: newStatus }),
        })
        .then((res) => {
            if (!res.ok) throw new Error("Lỗi cập nhật trạng thái");
            // Cập nhật lại danh sách đơn hàng trong state
            setOrders((prevOrders) =>
                prevOrders.map((order) =>
                    order.orderId === orderId ? { ...order, status: newStatus } : order
                )
            );
        })
        .catch((err) => alert("Không thể cập nhật trạng thái: " + err.message));
    };

    if (loading) return <p className="text-center text-lg">Đang tải danh sách đơn hàng...</p>;
    if (error) return <p className="text-center text-red-500 text-lg">Lỗi: {error}</p>;

    return (
        <div className="p-6 bg-gray-100 min-h-screen">
            <div className="bg-white shadow-md rounded-lg p-4 flex justify-between items-center mb-6">
                <h2 className="text-2xl font-semibold text-gray-800">Quản lý đơn hàng</h2>
                <Link to="/dashboard" className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition">
                    Quay lại Dashboard
                </Link>
            </div>

            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                <table className="w-full border-collapse">
                    <thead className="bg-blue-500 text-white">
                        <tr>
                            <th className="p-3 text-left">ID</th>
                            <th className="p-3 text-left">Tên khách hàng</th>
                            <th className="p-3 text-left">SĐT</th>
                            <th className="p-3 text-left">Địa chỉ</th>
                            <th className="p-3 text-right">Tổng tiền</th>
                            <th className="p-3 text-center">Trạng thái</th>
                            <th className="p-3 text-center">Ngày đặt</th>
                            <th className="p-3 text-center">Chi tiết</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orders.map((order, index) => (
                            <tr key={order.orderId} className={index % 2 === 0 ? "bg-gray-50" : "bg-white"}>
                                <td className="p-3">{order.orderId}</td>
                                <td className="p-3">{order.customerName || "Không có thông tin"}</td>
                                <td className="p-3">{order.phone || "Không có thông tin"}</td>
                                <td className="p-3">{order.address || "Không có thông tin"}</td>
                                <td className="p-3 text-right">${order.totalPrice.toLocaleString("vi-VN")}</td>
                                <td className="p-3 text-center">
                                    <select
                                        value={order.status}
                                        onChange={(e) => handleStatusChange(order.orderId, e.target.value)}
                                        className="px-2 py-1 border rounded text-sm"
                                    >
                                        <option value="pending">Chờ xử lý</option>
                                        <option value="completed">Hoàn thành</option>
                                        <option value="cancelled">Đã huỷ</option>
                                    </select>
                                </td>
                                <td className="p-3 text-center">
                                    {order.orderDate ? new Date(order.orderDate).toLocaleDateString("vi-VN") : "Không xác định"}
                                </td>
                                <td className="p-3 text-center">
                                    <Link
                                        to={`/orders/${order.orderId}`}
                                        className="px-3 py-1 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition"
                                    >
                                        Xem
                                    </Link>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default OrderList;
