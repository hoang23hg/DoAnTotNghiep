import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: 'localhost',  // Có thể đặt '0.0.0.0' nếu muốn truy cập từ máy khác
    port: 5174, // Đặt cổng cố định
    strictPort: true, // Nếu cổng 5174 bị chiếm, không tự động chọn cổng khác
  }
})
