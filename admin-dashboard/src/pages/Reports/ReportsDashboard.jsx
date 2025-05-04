import React, { useState, useEffect } from "react";
import { Table, Card, DatePicker, Select, Button, message, Typography } from "antd";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from "recharts";
import axios from "axios";
import moment from "moment";
import * as XLSX from "xlsx";

const { Option } = Select;
const { Title } = Typography;

const ReportsDashboard = () => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [reportType, setReportType] = useState("daily");
    const [date, setDate] = useState(moment());
    const [month, setMonth] = useState(moment().month() + 1);
    const [year, setYear] = useState(moment().year());
    const [totalRevenue, setTotalRevenue] = useState(0);

    useEffect(() => {
        fetchReportData();
    }, [reportType, date, month, year]);

    const fetchReportData = async () => {
        setLoading(true);
        let url = `http://localhost:8080/api/reports/sales/${reportType}`;

        try {
            if (reportType === "daily") {
                url += `?date=${date.format("YYYY-MM-DD")}`;
            } else if (reportType === "monthly") {
                url += `?month=${month}&year=${year}`;
            } else if (reportType === "yearly") {
                url += `?year=${year}`;
            }

            const response = await axios.get(url);
            const reportData = response.data || [];
            setData(reportData);
            
            // Tính tổng doanh thu
            const total = reportData.reduce((sum, item) => sum + item.totalRevenue, 0);
            setTotalRevenue(total);
        } catch (error) {
            console.error("Lỗi khi lấy dữ liệu:", error);
            message.error("Không thể tải dữ liệu. Vui lòng thử lại!");
        } finally {
            setLoading(false);
        }
    };

    const exportToExcel = () => {
        // Định dạng lại dữ liệu
        const formattedData = data.map((item, index) => ({
            "STT": index + 1,
            "Tên sản phẩm": item.productName,
            "Số lượng bán": item.quantitySold,
            "Doanh thu ($)": item.totalRevenue,
        }));

        // Thêm dòng tổng doanh thu
        formattedData.push({
            "STT": "",
            "Tên sản phẩm": "👉 Tổng doanh thu",
            "Số lượng bán": "",
            "Doanh thu ($)": totalRevenue,
        });

        // Tạo worksheet bắt đầu từ dòng A3
        const worksheet = XLSX.utils.json_to_sheet(formattedData, { origin: "A3" });

        // Thêm tiêu đề báo cáo
        const title =
            reportType === "daily"
                ? `Báo cáo doanh số ngày ${date.format("DD/MM/YYYY")}`
                : reportType === "monthly"
                ? `Báo cáo doanh số tháng ${month}/${year}`
                : `Báo cáo doanh số năm ${year}`;

        XLSX.utils.sheet_add_aoa(worksheet, [[title]], { origin: "A1" });

        // Cài đặt độ rộng cột
        worksheet["!cols"] = [
            { wch: 5 },
            { wch: 30 },
            { wch: 15 },
            { wch: 20 },
        ];

        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "Báo cáo doanh số");
        XLSX.writeFile(workbook, `baocao_doanhso_${reportType}.xlsx`);
    };

    const columns = [
        { title: "Tên sản phẩm", dataIndex: "productName", key: "productName" },
        { title: "Số lượng bán", dataIndex: "quantitySold", key: "quantitySold" },
        { title: "Doanh thu", dataIndex: "totalRevenue", key: "totalRevenue" },
    ];

    return (
        <Card title="📊 Báo cáo doanh số" style={{ maxWidth: 1000, margin: "auto" }}>
            <div style={{ marginBottom: 16, display: "flex", gap: "10px", flexWrap: "wrap" }}>
                <Select value={reportType} onChange={setReportType} style={{ width: 120 }}>
                    <Option value="daily">Ngày</Option>
                    <Option value="monthly">Tháng</Option>
                    <Option value="yearly">Năm</Option>
                </Select>

                {reportType === "daily" && (
                    <DatePicker value={date} onChange={setDate} format="YYYY-MM-DD" />
                )}

                {reportType === "monthly" && (
                    <>
                        <Select value={month} onChange={setMonth} style={{ width: 100 }}>
                            {Array.from({ length: 12 }, (_, i) => (
                                <Option key={i + 1} value={i + 1}>{`Tháng ${i + 1}`}</Option>
                            ))}
                        </Select>
                        <Select value={year} onChange={setYear} style={{ width: 100 }}>
                            {Array.from({ length: 5 }, (_, i) => (
                                <Option key={year - i} value={year - i}>{year - i}</Option>
                            ))}
                        </Select>
                    </>
                )}

                {reportType === "yearly" && (
                    <Select value={year} onChange={setYear} style={{ width: 100 }}>
                        {Array.from({ length: 5 }, (_, i) => (
                            <Option key={year - i} value={year - i}>{year - i}</Option>
                        ))}
                    </Select>
                )}

                <Button type="primary" onClick={fetchReportData} loading={loading}>
                    Tải dữ liệu
                </Button>
                <Button onClick={exportToExcel}>Xuất Excel</Button>
            </div>

            <Title level={4}>Tổng doanh thu: ${totalRevenue.toLocaleString()}</Title>

            <Table 
                dataSource={data} 
                columns={columns} 
                rowKey="productName"
                loading={loading}
                bordered
            />

            <ResponsiveContainer width="100%" height={300}>
                <BarChart data={data}>
                    <XAxis dataKey="productName" />
                    <YAxis />
                    <Tooltip />
                    <Bar dataKey="totalRevenue" fill="#8884d8" />
                </BarChart>
            </ResponsiveContainer>
        </Card>
    );
};

export default ReportsDashboard;
