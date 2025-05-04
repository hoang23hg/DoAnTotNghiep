import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import MainLayout from "../layout/MainLayout";
import Dashboard from "../pages/Dashboard";
import UsersList from "../pages/Users/UsersList";
import OrdersList from "../pages/Orders/OrdersList";
import OrderDetail from "../pages/Orders/OrderDetail";
import CategoriesList from "../pages/Categories/CategoriesList";
import ProductsList from "../pages/Products/ProductsList";
import ProductSizeList from "../pages/Products/ProductSizeList";
import ReportsDashboard from "../pages/Reports/ReportsDashboard";
import Login from "../pages/Login";

const AppRoutes = () => {
    return (
        <Router>
            <Routes>
                {/* Trang login độc lập, không nằm trong MainLayout */}
                <Route path="/login" element={<Login />} />

                {/* Các route khác nằm trong MainLayout */}
                <Route path="/" element={<MainLayout />}>
                    <Route index element={<Navigate to="/dashboard" />} /> 
                    <Route path="dashboard" element={<Dashboard />} />
                    <Route path="users" element={<UsersList />} />
                    <Route path="orders" element={<OrdersList />} />
                    <Route path="orders/:id" element={<OrderDetail />} />
                    <Route path="categories" element={<CategoriesList />} />
                    <Route path="products" element={<ProductsList />} />
                    <Route path="product-sizes" element={<ProductSizeList />} />
                    <Route path="reports" element={<ReportsDashboard />} />
                </Route>
            </Routes>
        </Router>
    );
};

export default AppRoutes;
