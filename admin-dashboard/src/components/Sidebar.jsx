import { Link } from "react-router-dom";

const Sidebar = () => {
    return (
        <div className="w-64 bg-gray-800 text-white p-4 h-screen fixed left-0 top-0 overflow-y-auto shadow-lg">
            <h2 className="text-2xl font-bold mb-4">Admin Dashboard</h2>
            <ul className="space-y-2">
                <li>
                    <Link to="/" className="block py-2 px-4 hover:bg-gray-700 rounded">
                        📊 Dashboard
                    </Link>
                </li>
                <li>
                    <Link to="/users" className="block py-2 px-4 hover:bg-gray-700 rounded">
                        👤 Quản lý người dùng
                    </Link>
                </li>
                <li>
                    <Link to="/orders" className="block py-2 px-4 hover:bg-gray-700 rounded">
                        📦 Quản lý đơn hàng
                    </Link>
                </li>
                <li>
                    <Link to="/categories" className="block py-2 px-4 hover:bg-gray-700 rounded">
                        🏷️ Quản lý danh mục
                    </Link>
                </li>
                <li>
                    <div className="px-4 py-2 hover:bg-gray-700 rounded cursor-pointer">
                        🛒 Quản lý sản phẩm
                    </div>
                    <ul className="ml-4 mt-1 space-y-1 text-sm">
                        <li>
                            <Link to="/products" className="block py-1 px-4 hover:bg-gray-700 rounded">
                                📋 Danh sách sản phẩm
                            </Link>
                        </li>
                        <li>
                            <Link to="/product-sizes" className="block py-1 px-4 hover:bg-gray-700 rounded">
                                ℹ️ Thông tin sản phẩm
                            </Link>
                        </li>
                    </ul>
                </li>

                <li>
                    <Link to="/reports" className="block py-2 px-4 hover:bg-gray-700 rounded">
                        📈 Báo cáo thống kê
                    </Link>
                </li>
            </ul>
        </div>
    );
};

export default Sidebar;
