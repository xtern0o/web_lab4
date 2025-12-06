import axios from 'axios';

class ConfigService {
    constructor() {
        this.config = null;
        this.error = null;
        this.loading = false;
        this.promise = null; // для предотвращения двойной загрузки
    }

    async loadConfig() {
        if (this.config) return this.config;
        if (this.promise) return this.promise;
        

        this.loading = true;
        this.promise = (async () => {
            try {
                const response = await axios.get(import.meta.env.VITE_API_URL + '/auth/config', {
                    timeout: parseInt(import.meta.env.VITE_API_TIMEOUT) || 5000,
                });
                this.config = response.data;
                this.error = null;
                return this.config;
            } catch (err) {
                console.error("failed to load config:", err);
                this.error = err;
                this.config = null;
                throw err;
            } finally {
                this.loading = false;
                this.promise = null;
            }
        })();

        return this.promise;

    }

    getConfig() {
        if (!this.config) {
            throw new Error("Config not loaded yet");
        }
        return this.config;
    }

    clearConfig() {
        this.config = null;
        this.error = null;
        this.loading = false;
        this.promise = null;
    }
}

export const configService = new ConfigService();