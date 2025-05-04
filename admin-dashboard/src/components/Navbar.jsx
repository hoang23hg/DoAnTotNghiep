import { useNavigate } from "react-router-dom";
import { auth } from "../firebase";
import { signOut } from "firebase/auth";

const Navbar = () => {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await signOut(auth); // Đăng xuất Firebase
            localStorage.removeItem("isAdmin"); // Xóa trạng thái đăng nhập
            sessionStorage.removeItem("isAdmin"); // Xóa thêm trong sessionStorage (nếu có)
            navigate("/login"); // Điều hướng về trang đăng nhập
        } catch (error) {
            console.error("Lỗi khi đăng xuất:", error);
        }
    };

    return (
        <div className="bg-white shadow-md p-4 flex justify-between items-center">
            <h2 className="text-lg font-semibold">Quản trị viên</h2>
            <button 
                onClick={handleLogout} 
                className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition"
            >
                Đăng xuất
            </button>
        </div>
    );
};

export default Navbar;
