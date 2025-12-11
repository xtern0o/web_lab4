<template>

<h1>Админка</h1>
<div style="display: flex;">
    <button @click="deleteAllPoints()" style="flex-grow: 1;">Удалить ВСЕ точки</button>
</div>
<Transition apper name="error-fade">
    <div v-if="success" class="success-container">
        <p class="success-container-header">Успешно</p>
        <p>Все точки успешно удалены!</p>
    </div>
</Transition>

</template>

<style lang="less">
    .error-fade-enter-active,
    .error-fade-leave-active {
        transition: all 0.4s ease;
    }

    .error-fade-enter-from,
    .error-fade-leave-to {
        opacity: 0;
        transform: translateY(20px);
    }
    
</style>

<script setup>
import { onBeforeMount, ref } from 'vue';
import { useRouter } from 'vue-router'
import { authService } from '../services/authService'
import api from '../services/api'

const hasPermission = ref(false);
const router = useRouter();
const success = ref(false);

async function deleteAllPoints() {
    try {
        const response = await api.delete("/points");
        success.value = true;
        setTimeout(() => {
            success.value = false;
        }, 3000);
    } catch(err) {
        alert("error");
    }
}

onBeforeMount(() => {
    hasPermission.value = authService.isAdmin();
    if (!hasPermission.value) {
        router.push('/points');
    }
})
</script>