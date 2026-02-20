import path from "path"
import react from "@vitejs/plugin-react"
import { defineConfig } from "vite"

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/cards': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/card-request-types': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/card-requests': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      }
    }
  },
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
})
