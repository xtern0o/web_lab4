import { createApp } from 'vue'
import './styles/style.less'
import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(router)

export const API_CONFIG = {
  apiUrl: import.meta.env.VITE_API_URL,
  timeout: parseInt(import.meta.env.VITE_API_TIMEOUT) || 5000
};

app.provide('apiConfig', API_CONFIG);

app.mount('#app')
