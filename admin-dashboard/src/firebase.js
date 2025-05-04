// src/firebase.js
import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider, signInWithRedirect, signInWithPopup } from "firebase/auth";
import { getDatabase, ref, get } from "firebase/database";

const firebaseConfig = {
  apiKey: "AIzaSyBH-8LmB14epL-YNBSuo4Fw041s_D-pnTw",
  authDomain: "cauten-d1c93.firebaseapp.com",
  databaseURL: "https://cauten-d1c93-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "cauten-d1c93",
  storageBucket: "cauten-d1c93.appspot.com",
  messagingSenderId: "979602285050",
  appId: "1:979602285050:web:7c63ac55bde2145c7e8443",
  measurementId: "G-7847X5KBNZ"
};

// Khởi tạo Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const database = getDatabase(app);
const provider = new GoogleAuthProvider();
provider.setCustomParameters({ prompt: "select_account" });

export { auth, database, provider,ref, get, signInWithPopup, signInWithRedirect };
