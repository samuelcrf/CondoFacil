import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// Configuração do Vite (a ferramenta que roda e compila o front-end).
export default defineConfig({
  // Plugin que ensina o Vite a entender React (JSX).
  plugins: [react()],

  server: {
    // Porta do front-end durante o desenvolvimento: http://localhost:5173
    port: 5173,

    // PROXY: todo pedido que começar com "/api" é repassado para o back-end
    // (Spring Boot, porta 8080). Assim o React chama "/api/saude" como se a API
    // estivesse no mesmo endereço, e não precisamos configurar CORS em desenvolvimento.
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
