import axios from "axios";
import { authService } from "./authService";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || '/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

// интерсептор на запросы для подстановки Bearer токена автоматически!!
api.interceptors.request.use(
    (config) => {
        const token = authService.getAccessToken();
        if (token) config.headers.Authorization = `Bearer ${token}`
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
)

export default api;