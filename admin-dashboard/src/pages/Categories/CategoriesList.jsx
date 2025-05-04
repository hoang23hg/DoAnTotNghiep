import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";

const CategoryList = () => {
    const [categories, setCategories] = useState([]);
    const [search, setSearch] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [newCategory, setNewCategory] = useState({ name: "", image: "" });
    const [editCategory, setEditCategory] = useState(null);

    useEffect(() => {
        fetchCategories();
    }, []);

    const fetchCategories = () => {
        fetch("http://localhost:8080/api/categories")
            .then((res) => res.json())
            .then((data) => {
                setCategories(data);
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setLoading(false);
            });
    };

    const handleSearch = (event) => {
        setSearch(event.target.value);
    };

    const handleAddCategory = () => {
        if (!newCategory.name || !newCategory.image) {
            alert("Vui lòng nhập đủ thông tin!");
            return;
        }
        
        fetch("http://localhost:8080/api/categories", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                categoryName: newCategory.name,
                imageUrl: newCategory.image,
            }),
        }).then(() => {
            fetchCategories();
            setNewCategory({ name: "", image: "" });
        });
    };

    const handleEditCategory = () => {
        if (!editCategory || !editCategory.categoryName || !editCategory.imageUrl) {
            alert("Vui lòng nhập đủ thông tin!");
            return;
        }

        fetch(`http://localhost:8080/api/categories/${editCategory.categoryId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(editCategory),
        }).then(() => {
            fetchCategories();
            setEditCategory(null);
        });
    };

    const handleDeleteCategory = (id) => {
        if (window.confirm("Bạn có chắc chắn muốn xóa danh mục này?")) {
            fetch(`http://localhost:8080/api/categories/${id}`, { method: "DELETE" })
                .then(() => fetchCategories());
        }
    };

    const filteredCategories = categories.filter(category =>
        category.categoryName.toLowerCase().includes(search.toLowerCase())
    );

    if (loading) return <p className="text-center text-lg">Đang tải danh sách danh mục...</p>;
    if (error) return <p className="text-center text-red-500 text-lg">Lỗi: {error}</p>;

    return (
        <div className="flex justify-center p-6 bg-gray-100 min-h-screen">
            <div className="bg-white shadow-lg rounded-lg p-6 w-full max-w-5xl">
                {/* Navbar */}
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-2xl font-semibold text-gray-800">Quản lý danh mục</h2>
                    <Link to="/dashboard" className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600">
                        Quay lại Dashboard
                    </Link>
                </div>

                {/* Thêm danh mục */}
                <div className="bg-gray-50 p-4 rounded-lg mb-6">
                    <h3 className="text-lg font-semibold mb-2">Thêm danh mục</h3>
                    <div className="flex gap-4">
                        <input
                            type="text"
                            placeholder="Tên danh mục"
                            className="p-2 border rounded w-1/2"
                            value={newCategory.name}
                            onChange={(e) => setNewCategory({ ...newCategory, name: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="Tên ảnh (vd: category1.jpg)"
                            className="p-2 border rounded w-1/2"
                            value={newCategory.image}
                            onChange={(e) => setNewCategory({ ...newCategory, image: e.target.value })}
                        />
                        <button onClick={handleAddCategory} className="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600">
                            Thêm
                        </button>
                    </div>
                </div>

                {/* Tìm kiếm */}
                <div className="mb-4">
                    <input
                        type="text"
                        placeholder="Tìm kiếm danh mục..."
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={search}
                        onChange={handleSearch}
                    />
                </div>

                {/* sửa danh mục */}
                {editCategory && (
                <div className="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center">
                    <div className="bg-white p-6 rounded-lg shadow-md">
                        <h3 className="text-lg font-semibold mb-4">Chỉnh sửa danh mục</h3>
                        <input
                            type="text"
                            className="p-2 border rounded w-full mb-3"
                            value={editCategory.categoryName}
                            onChange={(e) =>
                                setEditCategory({ ...editCategory, categoryName: e.target.value })
                            }
                        />
                        <input
                            type="text"
                            className="p-2 border rounded w-full mb-3"
                            value={editCategory.imageUrl}
                            onChange={(e) =>
                                setEditCategory({ ...editCategory, imageUrl: e.target.value })
                            }
                        />
                        <div className="flex justify-end space-x-2">
                            <button
                                onClick={() => setEditCategory(null)}
                                className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600"
                            >
                                Hủy
                            </button>
                            <button
                                onClick={handleEditCategory}
                                className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
                            >
                                Cập nhật
                            </button>
                        </div>
                    </div>
                </div>
            )}

                {/* Danh sách danh mục */}
                <div className="overflow-x-auto">
                    <table className="w-full border-collapse text-center">
                        <thead className="bg-blue-500 text-white">
                            <tr>
                                <th className="p-3">ID</th>
                                <th className="p-3">Tên danh mục</th>
                                <th className="p-3">Hình ảnh</th>
                                <th className="p-3">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredCategories.map((category, index) => (
                                <tr key={category.categoryId} className={index % 2 === 0 ? "bg-gray-50" : "bg-white"}>
                                    <td className="p-3">{category.categoryId}</td>
                                    <td className="p-3">{category.categoryName}</td>
                                    <td className="p-3">
                                        <img
                                            src={`http://localhost/android_api/categories/${category.imageUrl}`}
                                            alt={category.categoryName}
                                            className="h-20 w-20 object-cover mx-auto rounded-lg shadow"
                                        />
                                    </td>
                                    <td className="p-3 space-x-2">
                                        <button
                                            onClick={() => setEditCategory(category)}
                                            className="px-3 py-1 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600"
                                        >
                                            Sửa
                                        </button>
                                        <button
                                            onClick={() => handleDeleteCategory(category.categoryId)}
                                            className="px-3 py-1 bg-red-500 text-white rounded-lg hover:bg-red-600"
                                        >
                                            Xóa
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default CategoryList;
