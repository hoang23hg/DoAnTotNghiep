import axios from "axios";

const API_URL = "http://localhost:8080/api/users"; // Địa chỉ backend

// Lấy danh sách tất cả người dùng
export const getAllUsers = async () => {
    try {
        const response = await axios.get(API_URL);
        return response.data;
    } catch (error) {
        console.error("Lỗi khi lấy danh sách người dùng:", error);
        throw error;
    }
};

// Lấy thông tin một người dùng theo ID
export const getUserById = async (uid) => {
    try {
        const response = await axios.get(`${API_URL}/${uid}`);
        return response.data;
    } catch (error) {
        console.error("Lỗi khi lấy thông tin người dùng:", error);
        throw error;
    }
};

// Thêm người dùng mới
export const createUser = async (user) => {
    try {
        const response = await axios.post(API_URL, user);
        return response.data;
    } catch (error) {
        console.error("Lỗi khi tạo người dùng:", error);
        throw error;
    }
};

// Cập nhật thông tin người dùng
export const updateUser = async (uid, updatedUser) => {
    try {
        const response = await axios.put(`${API_URL}/${uid}`, updatedUser);
        return response.data;
    } catch (error) {
        console.error("Lỗi khi cập nhật người dùng:", error);
        throw error;
    }
};

// Xóa người dùng
export const deleteUser = async (uid) => {
    try {
        await axios.delete(`${API_URL}/${uid}`);
    } catch (error) {
        console.error("Lỗi khi xóa người dùng:", error);
        throw error;
    }
};
