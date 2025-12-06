<template>
    <div class="callback-error-container">
        <div v-if="error" class="error-container">
            <img src="../assets/img/danger-18465_256.gif" />
            <div>
                <p class="error-container-header">Ошибка авторизации</p>
                <p>{{ error }}</p>
            </div>
        </div>
    </div>
</template>
<script setup>
import { onBeforeMount, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { authService } from '../services/authService';

const router = useRouter();
const route = useRoute();
const processed = ref(null);
const error = ref(null);


onBeforeMount(async () => {
    if (processed.value) return;

    processed.value = true;

    try {
        const code = route.query.code;
        if (!code) {
            throw new Error("No Auth code");
        }

        const data = await authService.handleCallback(code);

        console.log("successfully exchanged code fot tokens!");

        router.push("/points");

    } catch (err) {
        console.error("ОШИБКУ ПОЙМАЛИ В CALLBACK.VUE", err);
        if (err.response?.data) {
            const errorData = err.response.data;
            if (errorData.keycloak_details) {
                error.value = `${errorData.keycloak_details.error}: ${errorData.keycloak_details.error_description}`
            } else {
                error.value = errorData.error_description || errorData.message || 'auth error'
            }
        } else {
            error.value = err.message || 'что-то пошло не так, не удалось подключиться к серверу'
        }
    }
})

</script> 

<style lang="less" scoped>
.callback-error-container {
    margin: 2rem 0;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>