<script setup>
import { ref, computed, onBeforeMount, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router'
import { authService } from '../services/authService';

const router = useRouter();
const route = useRoute();

const userInfo = ref(null);

const isAuthenticated = ref(false);

const navigationLinks = computed(() => {
	const links = [
		{ name: 'Главная', path: '/' },
		{ name: 'Точки', path: '/points' },
	];
	
	if (!isAuthenticated.value) {
        links.push({ name: 'Войти', path: '/auth' });
    }
	
	return links;
});

function updateAuthState() {
    isAuthenticated.value = authService.isAuthenticated();
    if (isAuthenticated.value) userInfo.value = authService.getUserInfo();
    else userInfo.value = null;
    console.log("authState updated: " + isAuthenticated.value);
    console.log(userInfo.value)
}

onBeforeMount(() => {
    updateAuthState();
});

watch(() => route.path, () => {
    updateAuthState();
});

async function logout() {
	authService.logout();
    updateAuthState();
};

</script>

<template>
<div class="navbar">
    <div class="navbar-brand">
        <p class="navbar-brand-link">P3211</p>
        <p class="navbar-brand-link">Карнажицкий Максим Романович</p>
        <p class="navbar-brand-link">Лаб 4</p>
        <p class="navbar-brand-link">8281</p>
    </div>

    <nav class="navbar-nav">
        <router-link
            v-for="link in navigationLinks"
            :key="link.path"
            class="navbar-nav-item-link"
            :to="link.path"
            exact-active-class="here">
            {{ link.name }}
        </router-link>

        <div class="navbar-nav-item-user" v-if="isAuthenticated && userInfo">
            <a>{{ userInfo.name + " (" + userInfo.preferred_username + ")" }}</a>
            <div class="dropdown-content">
                <p>{{ userInfo.email }}</p>
                <a href="/admin" v-if="authService.isAdmin()">Админка</a>
                <a @click="logout" class="logout">Выйти</a>
            </div>
        </div>
    </nav>
</div>
</template>

<style lang="less" scoped>
@import '../styles/style.less';

</style>