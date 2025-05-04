import React, { useEffect, useState } from "react";
import axios from "axios";

function ProductSizeList() {
  const [groupedData, setGroupedData] = useState({});
  const [editMode, setEditMode] = useState({});
  const [editedQuantity, setEditedQuantity] = useState({});
  const [loading, setLoading] = useState(true);
  const [newSizeData, setNewSizeData] = useState({});
  const [sizeOptions, setSizeOptions] = useState([]);

  useEffect(() => {
    fetchProductSizes();
    fetchSizeOptions();
  }, []);

  const fetchProductSizes = () => {
    axios
      .get("http://localhost:8080/api/product-sizes/details")
      .then((res) => {
        groupByProductId(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Lỗi khi lấy dữ liệu:", err);
        setLoading(false);
      });
  };

  const fetchSizeOptions = () => {
    axios
      .get("http://localhost:8080/api/sizes")
      .then((res) => {
        setSizeOptions(res.data);
      })
      .catch((err) => {
        console.error("Lỗi khi lấy danh sách size:", err);
      });
  };

  const groupByProductId = (data) => {
    const grouped = data.reduce((acc, item) => {
      const key = item.productId;
      if (!acc[key]) {
        acc[key] = { productName: item.productName, sizes: [] };
      }
      acc[key].sizes.push(item);
      return acc;
    }, {});
    setGroupedData(grouped);
  };

  const handleEditClick = (key, stockQuantity) => {
    setEditMode({ ...editMode, [key]: true });
    setEditedQuantity({ ...editedQuantity, [key]: stockQuantity });
  };

  const handleQuantityChange = (key, value) => {
    setEditedQuantity({ ...editedQuantity, [key]: value });
  };

  const handleSaveClick = (productId, sizeId) => {
    const key = `${productId}-${sizeId}`;
    const updatedStock = parseInt(editedQuantity[key]);

    axios
      .put("http://localhost:8080/api/product-sizes", {
        productId,
        sizeId,
        stockQuantity: updatedStock,
      })
      .then(() => {
        setEditMode({ ...editMode, [key]: false });
        fetchProductSizes();
      })
      .catch((err) => {
        console.error("Lỗi khi cập nhật:", err);
        alert("Cập nhật thất bại.");
      });
  };

  const handleDeleteClick = (productId, sizeId) => {
    axios
      .delete(`http://localhost:8080/api/product-sizes/${productId}/${sizeId}`)
      .then(() => {
        fetchProductSizes();
      })
      .catch((err) => {
        console.error("Lỗi khi xóa:", err);
      });
  };

  const handleNewSizeChange = (productId, field, value) => {
    setNewSizeData((prev) => ({
      ...prev,
      [productId]: {
        ...prev[productId],
        [field]: value,
      },
    }));
  };

  const handleAddSize = (productId) => {
    const sizeData = newSizeData[productId];
    if (!sizeData || !sizeData.sizeId || !sizeData.stockQuantity) {
      alert("Vui lòng chọn size và nhập tồn kho.");
      return;
    }

    axios
      .post("http://localhost:8080/api/product-sizes", {
        productId: parseInt(productId),
        sizeId: parseInt(sizeData.sizeId),
        stockQuantity: parseInt(sizeData.stockQuantity),
      })
      .then(() => {
        fetchProductSizes();
        setNewSizeData((prev) => ({ ...prev, [productId]: {} }));
      })
      .catch((err) => {
        console.error("Lỗi khi thêm size:", err);
        alert("Thêm thất bại. Có thể size đã tồn tại hoặc dữ liệu không hợp lệ.");
      });
  };

  if (loading) {
    return <p>Đang tải dữ liệu...</p>;
  }

  return (
    <div className="space-y-8 p-4">
      {Object.entries(groupedData).map(([productId, data]) => (
        <div key={`group-${productId}`} className="border rounded-xl p-4 shadow bg-white">
          <h2 className="text-lg font-semibold mb-3">{data.productName}</h2>
          <table className="w-full text-sm border border-gray-200 mb-4">
            <thead className="bg-gray-100">
              <tr>
                <th className="text-left p-2 border">Size</th>
                <th className="text-left p-2 border">Tồn kho</th>
                <th className="text-left p-2 border">Hành động</th>
              </tr>
            </thead>
            <tbody>
              {data.sizes.map((item) => {
                const key = `${item.productId}-${item.sizeId}`;
                return (
                  <tr key={`row-${key}`}>
                    <td className="p-2 border">{item.sizeName}</td>
                    <td className="p-2 border">
                      {editMode[key] ? (
                        <input
                          type="number"
                          className="border px-2 py-1 w-20"
                          value={editedQuantity[key] || ""}
                          onChange={(e) => handleQuantityChange(key, e.target.value)}
                        />
                      ) : (
                        item.stockQuantity
                      )}
                    </td>
                    <td className="p-2 border space-x-2">
                      {editMode[key] ? (
                        <button
                          className="bg-green-500 text-white px-3 py-1 rounded"
                          onClick={() => handleSaveClick(item.productId, item.sizeId)}
                        >
                          Lưu
                        </button>
                      ) : (
                        <button
                          className="bg-yellow-500 text-white px-3 py-1 rounded"
                          onClick={() => handleEditClick(key, item.stockQuantity)}
                        >
                          Sửa
                        </button>
                      )}
                      <button
                        className="bg-red-500 text-white px-3 py-1 rounded"
                        onClick={() => handleDeleteClick(item.productId, item.sizeId)}
                      >
                        Xóa
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>

          {/* Thêm size mới */}
          <div className="flex items-center gap-4">
            <select
              className="border px-3 py-1"
              value={newSizeData[productId]?.sizeId || ""}
              onChange={(e) => handleNewSizeChange(productId, "sizeId", e.target.value)}
            >
              <option value="">Chọn size</option>
              {sizeOptions.map((size) => {
                const isExisting = data.sizes.some((s) => s.sizeName === size);
                return (
                  <option
                    key={`new-${productId}-${size}`}
                    value={sizeOptions.indexOf(size) + 1} // giả định sizeId là index + 1
                    disabled={isExisting}
                  >
                    {size}
                  </option>
                );
              })}
            </select>
            <input
              type="number"
              className="border px-2 py-1 w-24"
              placeholder="Tồn kho"
              value={newSizeData[productId]?.stockQuantity || ""}
              onChange={(e) =>
                handleNewSizeChange(productId, "stockQuantity", e.target.value)
              }
            />
            <button
              className="bg-blue-600 text-white px-4 py-1 rounded"
              onClick={() => handleAddSize(productId)}
            >
              Thêm
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}

export default ProductSizeList;