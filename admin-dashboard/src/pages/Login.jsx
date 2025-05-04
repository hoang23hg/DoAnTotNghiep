// src/pages/Login.jsx
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { auth, database, provider, signInWithRedirect, signInWithPopup } from "../firebase";
import { onAuthStateChanged, signOut } from "firebase/auth";
import { ref, get } from "firebase/database";

const Login = () => {
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    onAuthStateChanged(auth, async (user) => {
      if (user) {
        // Kiểm tra role trong Realtime Database
        const userRef = ref(database, `Users/${user.uid}`);
        const snapshot = await get(userRef);

        if (snapshot.exists() && snapshot.val().role === "admin") {
          sessionStorage.setItem("isAdmin", "true");
          navigate("/dashboard");
        } else {
          setError("Bạn không có quyền admin!");
          signOut(auth);
        }
      }
    });
  }, [navigate]);

  const handleGoogleLogin = async () => {
    try {
      if (window.innerWidth < 600) {
        await signInWithRedirect(auth, provider);
      } else {
        await signInWithPopup(auth, provider);
      }
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <div className="w-screen h-screen flex justify-center items-center relative">
      {/* Background Gradient */}
      <div className="absolute inset-0 w-full h-full bg-gradient-to-r from-blue-500 to-purple-500"></div>

      {/* Form Login */}
      <div className="relative z-10 bg-white p-8 rounded-lg shadow-xl w-96">
        <h2 className="text-2xl font-bold mb-4 text-gray-700 text-center">Đăng Nhập</h2>

        {/* Hiển thị lỗi nếu có */}
        {error && <p className="text-red-500 text-sm mb-3">{error}</p>}

        <button
          onClick={handleGoogleLogin}
          className="w-full bg-blue-500 hover:bg-blue-600 text-white p-3 rounded font-semibold transition duration-200"
        >
          Đăng nhập với Google
        </button>
      </div>
    </div>
  );
};

export default Login;