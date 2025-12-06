import { configService } from "./configService";
import axios from 'axios'

class AuthService {
    constructor() {
        this.accessToken = null;
        this.refreshToken = null;
        this.idToken = null;
        this.expiresAt = null;
        this.refreshExpiresAt = null;
        this.apiUrl = import.meta.env.VITE_API_URL;
    }

    getAccessToken() {
        if (!this.accessToken) {
            this.accessToken = localStorage.getItem('access_token');
            const expiresAt = localStorage.getItem('expires_at');
            if (expiresAt) {
                this.expiresAt = parseInt(expiresAt);
            }
        }
        return this.accessToken;
    }

    getRefreshToken() {
        if (!this.refreshToken) {
            this.refreshToken = localStorage.getItem('refresh_token');
            const refreshExpiresAt = localStorage.getItem('refresh_expires_at');
            if (refreshExpiresAt) {
                this.refreshExpiresAt = parseInt(refreshExpiresAt, 10);
            }
        }
        return this.refreshToken;
    }

    getIdToken() {
        if (!this.idToken) {
            this.idToken = localStorage.getItem('id_token');
        }
        return this.idToken;
    }

    clearTokens() {
        this.accessToken = null;
        this.refreshToken = null;
        this.idToken = null;
        this.expiresAt = null;
        this.refreshExpiresAt = null;

        localStorage.removeItem('access_token');
        localStorage.removeItem('refresh_token');
        localStorage.removeItem('id_token');
        localStorage.removeItem('expires_at');
        localStorage.removeItem('refresh_expires_at');
        localStorage.removeItem('token_type');
        localStorage.removeItem('scope');
    }
    
    getUserInfo() {
        const idToken = this.getIdToken();
        const access_token = this.getAccessToken();
        if (!idToken || !access_token) {
            return null;
        }
        const idDecoded = JSON.parse(atob(idToken.split('.')[1]));
        const accessDecoded = JSON.parse(atob(access_token.split('.')[1]));
        return {
            sub: idDecoded.sub,
            email: idDecoded.email,
            name: idDecoded.name,
            given_name: idDecoded.given_name,
            family_name: idDecoded.family_name,
            preferred_username: idDecoded.preferred_username,
            roles: accessDecoded.realm_access?.roles || []
        }
    }

    getAccessTokenExpiresIn() {
        if (!this.expiresAt) return 0;
        const remaining = Math.floor((this.expiresAt - Date.now()) / 1000);
        return Math.max(0, remaining);
    }

    getRefreshTokenExpiresIn() {
        if (!this.refreshExpiresAt) return 0;
        const remaining = Math.floor((this.refreshExpiresAt - Date.now()) / 1000);
        return Math.max(0, remaining);
    }

    /**
     * Проверка, нужно ли обновлять токен (за 30 сек до истечения)
     * @returns 
     */
    shouldRefreshToken() {
        const expiresIn = this.getAccessTokenExpiresIn();
        return expiresIn > 0 && expiresIn < 30;
    }

    /**
     * аутентифицирован пользователь или нет
     * @returns authenticated?
     */
    isAuthenticated() {
        const token = this.getAccessToken();
        if (!token) return false;

        if (this.expiresAt && Date.now() > this.expiresAt) {
            console.log("access token expired. trying tu refresh...");
            try {
                this.refreshAccessToken();
            } catch (err) {
                console.error("failed to refresh access token:", err);
                return false;
            }
            if (Date.now() > this.expiresAt) {
                console.error("access token is still expired after refresh");
                return false;
            }
            return true;
        }

        return true;
    }

    async login() {
        try {
            const config = await configService.loadConfig()

            const redirectUri = `${window.location.origin}/callback`;

            const authUrl = new URL(`${config.auth_url}/realms/${config.realm}/protocol/openid-connect/auth`);
            authUrl.searchParams.set("client_id", config.client_id);
            authUrl.searchParams.set("redirect_uri", redirectUri);
            authUrl.searchParams.set("response_type", "code");
            authUrl.searchParams.set("scope", "openid");

            console.log("redirect to oauth2 page...");

            console.log(authUrl.toString())

            window.location.href = authUrl.toString();
        } catch (err) {
            console.error("error while redirecting to auth page:", err);
            throw err;
        }
    }

    async handleCallback(code) {
        try {
            const redirectUri = `${window.location.origin}/callback`;

            const response = await axios.post(
                `${this.apiUrl}/auth/callback`,
                {
                    code: code,
                    redirect_uri: redirectUri
                }
            )

            const data = response.data;
            this.saveData(data);
            console.log("successfully got auth data from server: ", data)

            return data;

        } catch (err) {
            console.error("error while handling callback from server: ", err)
            throw err;
        }
    }

    isRefreshTokenValid() {
        const refreshToken = this.getRefreshToken();
        if (!refreshToken) return false;

        if (this.refreshExpiresAt && Date.now() > this.refreshExpiresAt) {
            return false;
        }
        return true;
    }

    async refreshAccessToken() {
        if (!this.isRefreshTokenValid()) {
            console.error("refresh token is invalid. Impossible to update. Logging out...");
            this.clearTokens();
            throw new Error("Session expired");
        }

        const refreshToken = this.getRefreshToken();

        try {
            const response = await axios.post(
                `${this.apiUrl}/auth/refresh`,
                {
                    refresh_token: refreshToken
                }
            )
            const data = response.data;
            this.saveData(data);
            console.log("tokens are successfully refreshed");
            return data;
        } catch (err) {
            console.error("refresh token update error: ", err);
            if (err.response?.status === 401) {
                this.clearTokens();
                throw new Error("Session expired");
            }
            throw err;
        }
    }

    async logout() {
        this.clearTokens();
        location.reload();
        console.log("user logged out");
    }

    saveData(data) {
        this.accessToken = data.access_token;
        this.refreshToken = data.refresh_token;
        this.idToken = data.id_token;

        if (data.expires_in) {
            this.expiresAt = Date.now() + data.expires_in * 1000;
        }
        if (data.refresh_expires_in) {
            this.refreshExpiresAt = Date.now() + data.refresh_expires_in * 1000;
        }

        localStorage.setItem('access_token', data.access_token);
        localStorage.setItem('refresh_token', data.refresh_token);
        localStorage.setItem('id_token', data.id_token);
        localStorage.setItem('expires_at', this.expiresAt?.toString() || '');
        localStorage.setItem('refresh_expires_at', this.refreshExpiresAt?.toString() || '');

        console.log("auth data saved to localStorage");
    } 
    
}

export const authService = new AuthService();