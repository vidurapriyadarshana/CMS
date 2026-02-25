import path from "path"
import react from "@vitejs/plugin-react"
import { defineConfig, loadEnv } from "vite"

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const target = env.VITE_API_BASE_URL || 'http://localhost:8080'

  return {
    plugins: [react()],
    server: {
      proxy: {
        '/cards': {
          target,
          changeOrigin: true,
          secure: false,
        },
        '/card-request-types': {
          target,
          changeOrigin: true,
          secure: false,
        },
        '/card-requests': {
          target,
          changeOrigin: true,
          secure: false,
        },
        '/status': {
          target,
          changeOrigin: true,
          secure: false,
        },
        '/encryption': {
          target,
          changeOrigin: true,
          secure: false,
        },
        '/users': {
          target,
          changeOrigin: true,
          secure: false,
        },
        '/reports': {
          target,
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
  }
})
