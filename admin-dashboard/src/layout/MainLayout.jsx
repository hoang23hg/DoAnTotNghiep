import { Outlet, useLocation } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";

const MainLayout = () => {
    const location = useLocation();
    const isLoginPage = location.pathname === "/login"; // Kiểm tra trang Login

    return (
        <div className="flex">
            {!isLoginPage && <Sidebar />} {/* Ẩn Sidebar trên trang Login */}
            <div className={`flex-1 ${!isLoginPage ? "ml-64" : ""} flex flex-col min-h-screen`}>
                {!isLoginPage && <Navbar />}
                <main className="flex-grow p-4">
                    <Outlet />
                </main>
                {!isLoginPage && <Footer />}
            </div>
        </div>
    );
};

export default MainLayout;
