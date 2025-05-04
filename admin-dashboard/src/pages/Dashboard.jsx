import React, { useState, useEffect } from "react";
import { Card, Carousel } from "antd";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from "recharts";
import axios from "axios";

const Dashboard = () => {
  const [salesData, setSalesData] = useState([]);

  useEffect(() => {
    fetchSalesData();
  }, []);

  const fetchSalesData = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/reports/sales/yearly", {
        params: { month: new Date().getMonth() + 1, year: new Date().getFullYear() }
      });
      const formattedData = response.data.map(item => ({
        name: item.productName,
        revenue: item.totalRevenue
      }));
      setSalesData(formattedData);
    } catch (error) {
      console.error("Lỗi khi tải dữ liệu doanh thu:", error);
    }
  };

  return (
    <div className="container mx-auto max-w-screen-xl p-6">
      <h1 className="text-4xl font-bold mb-6 text-center">📊 Bảng Điều Khiển Quản Trị</h1>

      {/* Grid chia thành 2 cột */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">
        {/* Phần Carousel */}
        <div className="w-full h-36"> {/* Giảm chiều cao xuống 36 */}
          <Carousel autoplay className="h-full">
            <div>
              <img src="/images/banner1.png" alt="Khuyến mãi" className="w-full h-full object-cover rounded-lg" />
            </div>
            <div>
              <img src="/images/banner2.png" alt="Doanh thu" className="w-full h-full object-cover rounded-lg" />
            </div>
            <div>
              <img src="/images/banner3.jpg" alt="Báo cáo" className="w-full h-full object-cover rounded-lg" />
            </div>
          </Carousel>
        </div>

        {/* Biểu đồ doanh thu */}
        <Card title="📈 Doanh thu theo sản phẩm" className="shadow-lg p-6">
          <ResponsiveContainer width="100%" height={300}> {/* Giảm chiều cao để cân đối */}
            <BarChart data={salesData}>
              <XAxis dataKey="name" tick={{ fontSize: 14 }} />
              <YAxis tickFormatter={(value) => `${value / 1000}k`} />
              <Tooltip formatter={(value) => `${value.toLocaleString()} $`} />
              <Bar dataKey="revenue" fill="#3182CE" barSize={50} />
            </BarChart>
          </ResponsiveContainer>
        </Card>
      </div>
    </div>
  );
};

export default Dashboard;
