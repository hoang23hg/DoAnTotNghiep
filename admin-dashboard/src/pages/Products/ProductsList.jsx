import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";

const ProductList = () => {
    const [products, setProducts] = useState([]);
    const [search, setSearch] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState("");
    const [messageType, setMessageType] = useState(""); 

    const [newProduct, setNewProduct] = useState({
        name: "",
        description: "",
        price: "",
        stockQuantity: "",
        imageUrl: "",
        categoryId: ""
    });
    const [editProduct, setEditProduct] = useState(null);

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = () => {
        fetch("http://localhost:8080/api/products")
            .then((res) => res.json())
            .then((data) => {
                setProducts(data);
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

    const handleAddProduct = () => {
        if (!newProduct.name || !newProduct.price || !newProduct.categoryId || !newProduct.stockQuantity) {
            setMessage("Vui lòng nhập đủ thông tin!");
            setMessageType("error");
            return;
        }
    
        fetch("http://localhost:8080/api/products", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                name: newProduct.name,
                description: newProduct.description,
                price: parseFloat(newProduct.price),
                stockQuantity: parseInt(newProduct.stockQuantity),
                imageUrl: newProduct.imageUrl,
                category: { categoryId: parseInt(newProduct.categoryId) }
            }),
        })
        .then((res) => {
            if (!res.ok) {
                throw new Error("Lỗi khi thêm sản phẩm!");
            }
            return res.json();
        })
        .then(() => {
            setMessage("Thêm sản phẩm thành công!");
            setMessageType("success");
            fetchProducts();
            setNewProduct({ name: "", description: "", price: "", stockQuantity: "", imageUrl: "", categoryId: "" });
    
            setTimeout(() => setMessage(""), 3000); // Ẩn thông báo sau 3 giây
        })
        .catch(err => {
            setMessage("Lỗi: " + err.message);
            setMessageType("error");
            setTimeout(() => setMessage(""), 3000);
        });
    };
    
    

    const handleEditProduct = () => {
        if (!editProduct || !editProduct.name || !editProduct.price || !editProduct.categoryId || !editProduct.stockQuantity) {
            setMessage("Vui lòng nhập đủ thông tin!");
            setMessageType("error");
            return;
        }
    
        fetch(`http://localhost:8080/api/products/${editProduct.productId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                productId: editProduct.productId,
                name: editProduct.name,
                description: editProduct.description,
                price: parseFloat(editProduct.price),
                stockQuantity: parseInt(editProduct.stockQuantity),
                imageUrl: editProduct.imageUrl,
                category: { categoryId: parseInt(editProduct.categoryId) }
            }),
        })
        .then((res) => {
            if (!res.ok) {
                throw new Error("Lỗi khi chỉnh sửa sản phẩm!");
            }
            return res.json();
        })
        .then(() => {
            setMessage("Cập nhật sản phẩm thành công!");
            setMessageType("success");
            fetchProducts();
            setEditProduct(null);
    
            setTimeout(() => setMessage(""), 3000);
        })
        .catch(err => {
            setMessage("Lỗi: " + err.message);
            setMessageType("error");
            setTimeout(() => setMessage(""), 3000);
        });
    };
    
    
    const handleDeleteProduct = (id) => {
        if (window.confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
            fetch(`http://localhost:8080/api/products/${id}`, { method: "DELETE" })
                .then((res) => {
                    if (!res.ok) {
                        throw new Error("Lỗi khi xóa sản phẩm!");
                    }
                    return res.text();
                })
                .then(() => {
                    setMessage("Xóa sản phẩm thành công!");
                    setMessageType("success");
                    fetchProducts();
    
                    setTimeout(() => setMessage(""), 3000);
                })
                .catch(err => {
                    setMessage("Lỗi: " + err.message);
                    setMessageType("error");
                    setTimeout(() => setMessage(""), 3000);
                });
        }
    };
    

    const filteredProducts = products.filter(product =>
        product.name.toLowerCase().includes(search.toLowerCase())
    );

    if (loading) return <p className="text-center text-lg">Đang tải danh sách sản phẩm...</p>;
    if (error) return <p className="text-center text-red-500 text-lg">Lỗi: {error}</p>;

    return (
        
        <div className="flex justify-center p-6 bg-gray-100 min-h-screen">
            <div className="bg-white shadow-lg rounded-lg p-6 w-full max-w-5xl">
                {/* Navbar */}
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-2xl font-semibold text-gray-800">Quản lý sản phẩm</h2>
                    <Link to="/dashboard" className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600">
                        Quay lại Dashboard
                    </Link>
                </div>
                {/* Thông báo hiển thị khi thêm/sửa/xóa */}
                {message && (
                    <div className={`p-3 text-center text-white rounded-lg mb-4 ${messageType === "success" ? "bg-green-500" : "bg-red-500"}`}>
                        {message}
                    </div>
                )}
                {/* Thêm sản phẩm */}
                <div className="bg-gray-50 p-4 rounded-lg mb-6">
                    <h3 className="text-lg font-semibold mb-2">Thêm sản phẩm</h3>
                    <div className="grid grid-cols-3 gap-4">
                        <input type="text" placeholder="Tên sản phẩm" className="p-2 border rounded" value={newProduct.name} onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })} />
                        <input type="text" placeholder="Mô tả sản phẩm" className="p-2 border rounded" value={newProduct.description} onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })} />
                        <input type="number" placeholder="Giá" className="p-2 border rounded" value={newProduct.price} onChange={(e) => setNewProduct({ ...newProduct, price: e.target.value })} />
                        <input type="number" placeholder="Số lượng" className="p-2 border rounded" value={newProduct.stockQuantity} onChange={(e) => setNewProduct({ ...newProduct, stockQuantity: e.target.value })} />
                        <input type="text" placeholder="URL hình ảnh" className="p-2 border rounded" value={newProduct.imageUrl} onChange={(e) => setNewProduct({ ...newProduct, imageUrl: e.target.value })} />
                        <input type="number" placeholder="Mã danh mục" className="p-2 border rounded" value={newProduct.categoryId} onChange={(e) => setNewProduct({ ...newProduct, categoryId: e.target.value })} />
                        <button onClick={handleAddProduct} className="col-span-3 px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600">
                            Thêm
                        </button>
                    </div>
                </div>
                {/* Chỉnh sửa sản phẩm */}
                {editProduct && (
                    <div className="bg-gray-50 p-4 rounded-lg mb-6">
                        <h3 className="text-lg font-semibold mb-2">Chỉnh sửa sản phẩm</h3>
                        <div className="grid grid-cols-3 gap-4">
                            <input type="text" placeholder="Tên sản phẩm" className="p-2 border rounded" value={editProduct.name} onChange={(e) => setEditProduct({ ...editProduct, name: e.target.value })} />
                            <input type="text" placeholder="Mô tả" className="p-2 border rounded" value={editProduct.description} onChange={(e) => setEditProduct({ ...editProduct, description: e.target.value })} />
                            <input type="number" placeholder="Giá" className="p-2 border rounded" value={editProduct.price} onChange={(e) => setEditProduct({ ...editProduct, price: e.target.value })} />
                            <input type="number" placeholder="Số lượng" className="p-2 border rounded" value={editProduct.stockQuantity} onChange={(e) => setEditProduct({ ...editProduct, stockQuantity: e.target.value })} />
                            <input type="text" placeholder="URL hình ảnh" className="p-2 border rounded" value={editProduct.imageUrl} onChange={(e) => setEditProduct({ ...editProduct, imageUrl: e.target.value })} />
                            <input type="number" placeholder="Mã danh mục" className="p-2 border rounded" value={editProduct.categoryId} onChange={(e) => setEditProduct({ ...editProduct, categoryId: e.target.value })} />
                            <button onClick={handleEditProduct} className="col-span-3 px-4 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600">
                                Lưu
                            </button>
                            <button onClick={() => setEditProduct(null)} className="col-span-3 px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600">
                                Hủy
                            </button>
                        </div>
                    </div>
                )}

                {/* Tìm kiếm */}
                <div className="mb-4">
                    <input type="text" placeholder="Tìm kiếm sản phẩm..." className="w-full p-2 border rounded-lg" value={search} onChange={handleSearch} />
                </div>

                {/* Danh sách sản phẩm */}
                <div className="overflow-x-auto">
                    <table className="w-full border-collapse text-center">
                        <thead className="bg-blue-500 text-white">
                            <tr>
                                <th className="p-3">ID</th>
                                <th className="p-3">Tên</th>
                                <th className="p-3">Mô tả</th>
                                <th className="p-3">Giá</th>
                                <th className="p-3">Số lượng</th>
                                <th className="p-3">Hình ảnh</th>
                                <th className="p-3">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredProducts.map((product) => (
                                <tr key={product.productId} className="border-t">
                                    <td className="p-3">{product.productId}</td>
                                    <td className="p-3">{product.name}</td>
                                    <td className="p-3">{product.description}</td>
                                    <td className="p-3">${product.price}</td>
                                    <td className="p-3">{product.stockQuantity}</td>
                                    <td className="p-3">
                                        <img src={`http://localhost/android_api/products/${product.imageUrl}`} alt={product.name} className="h-16 w-16 object-cover rounded-lg" />
                                    </td>
                                    <td className="p-3 space-x-2">
                                        <button onClick={() => setEditProduct(product)} className="px-3 py-1 bg-yellow-500 text-white rounded hover:bg-yellow-600">Sửa</button>
                                        <button onClick={() => handleDeleteProduct(product.productId)} className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600">Xóa</button>
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

export default ProductList;
