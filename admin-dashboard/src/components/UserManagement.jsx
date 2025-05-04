import { useState, useEffect } from "react";
import axios from "axios";
import "./UserManagement.css"; // Import CSS

const UserManagement = () => {
    const [users, setUsers] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [filteredUsers, setFilteredUsers] = useState([]);
    const [userData, setUserData] = useState({
        uid: "", // Bổ sung UID để đảm bảo dữ liệu hợp lệ
        email: "",
        displayName: "",
        phone: ""
    });
    const [editingUser, setEditingUser] = useState(null);

    // Lấy danh sách người dùng
    const fetchUsers = () => {
        axios.get("http://localhost:8080/api/users")
            .then(response => {
                setUsers(response.data);
                setFilteredUsers(response.data);
            })
            .catch(error => console.error("Lỗi khi tải danh sách:", error));
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    // Cập nhật dữ liệu nhập
    const handleChange = (e) => {
        setUserData({ ...userData, [e.target.name]: e.target.value });
    };

    // Xử lý thêm / cập nhật người dùng
    const handleSaveUser = async () => {
        try {
            const payload = {
                uid: editingUser ? editingUser.uid : crypto.randomUUID(), // Tạo UID mới nếu thêm mới
                email: userData.email,
                displayName: userData.displayName,
                phone: userData.phone
            };

            if (editingUser) {
                await axios.put(`http://localhost:8080/api/users/${editingUser.uid}`, payload);
                alert("Cập nhật thành công!");
            } else {
                await axios.post("http://localhost:8080/api/users", payload);
                alert("Thêm người dùng thành công!");
            }

            setUserData({ uid: "", email: "", displayName: "", phone: "" });
            setEditingUser(null);
            fetchUsers();
        } catch (error) {
            console.error("Lỗi:", error.response?.data || error.message);
            alert("Lỗi khi thêm/cập nhật người dùng!");
        }
    };

    // Xóa người dùng
    const handleDeleteUser = async (uid) => {
        if (window.confirm("Bạn có chắc chắn muốn xóa người dùng này không?")) {
            try {
                await axios.delete(`http://localhost:8080/api/users/${uid}`);
                alert("Xóa thành công!");
                fetchUsers();
            } catch (error) {
                console.error("Lỗi khi xóa:", error);
            }
        }
    };

    // Đặt dữ liệu vào form để sửa
    const handleEditUser = (user) => {
        setUserData({
            uid: user.uid,
            email: user.email,
            displayName: user.displayName,
            phone: user.phone
        });
        setEditingUser(user);
    };

    // Xử lý tìm kiếm người dùng
    const handleSearch = (e) => {
        const term = e.target.value.toLowerCase();
        setSearchTerm(term);

        if (term === "") {
            setFilteredUsers(users);
        } else {
            const filtered = users.filter(user =>
                user.email.toLowerCase().includes(term) ||
                user.displayName.toLowerCase().includes(term) ||
                user.phone.includes(term)
            );
            setFilteredUsers(filtered);
        }
    };

    return (
        <div className="user-management">
            <h1 className="title">Quản lý Người Dùng</h1>

            {/* Form nhập liệu */}
            <div className="form-container">
                <input
                    type="text"
                    name="email"
                    placeholder="Email"
                    value={userData.email}
                    onChange={handleChange}
                />
                <input
                    type="text"
                    name="displayName"
                    placeholder="Tên hiển thị"
                    value={userData.displayName}
                    onChange={handleChange}
                />
                <input
                    type="text"
                    name="phone"
                    placeholder="Số điện thoại"
                    value={userData.phone}
                    onChange={handleChange}
                />
            </div>

            {/* Ô tìm kiếm và nút thêm nằm ngang hàng */}
            <div className="search-add-container">
                <input
                    type="text"
                    className="search-input"
                    placeholder="Tìm kiếm người dùng..."
                    value={searchTerm}
                    onChange={handleSearch}
                />
                <button className="add-btn" onClick={handleSaveUser}>
                    {editingUser ? "Cập nhật" : "Thêm"}
                </button>
            </div>

            {/* Bảng danh sách người dùng */}
            <table className="user-table">
                <thead>
                    <tr>
                        <th>UID</th>
                        <th>Email</th>
                        <th>Tên hiển thị</th>
                        <th>Số điện thoại</th>
                        <th>Địa chỉ</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredUsers.map(user => (
                        <tr key={user.uid}>
                            <td>{user.uid}</td>
                            <td>{user.email || "Chưa có email"}</td>
                            <td>{user.displayName || "Chưa có tên"}</td>
                            <td>{user.phone || "Chưa có số"}</td>
                            <td>
                                {Array.isArray(user.addresses) && user.addresses.length > 0 ? (
                                    user.addresses.map(addr => (
                                        <div key={addr.id}>
                                            {addr.street}, {addr.ward}, {addr.district}, {addr.city}
                                            {addr.isDefault && <span> (Mặc định)</span>}
                                        </div>
                                    ))
                                ) : "Chưa có địa chỉ"}
                            </td>
                            <td className="action-buttons">
                                <button
                                    className="edit-btn"
                                    onClick={() => handleEditUser(user)}
                                >
                                    Sửa
                                </button>
                                <button
                                    className="delete-btn"
                                    onClick={() => handleDeleteUser(user.uid)}
                                >
                                    Xóa
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default UserManagement;
