<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router'

const router = useRouter();

const authUserName = ref('');
const isAuth = ref(false);

const getUserName = () => {
    const localName = localStorage.getItem("authUserName");
    const sessionName = sessionStorage.getItem("authUserName");
    return localName || sessionName || '';
};

const checkAuth = () => {
    const hasLocalToken = localStorage.getItem("authToken") !== null;
    const hasSessionToken = sessionStorage.getItem("authToken") !== null;
    return hasLocalToken || hasSessionToken;
};

const navigationLinks = computed(() => {
	const links = [
		{ name: 'Главная', path: '/' },
		{ name: 'Точки', path: '/points' },
	];
	
	if (!isAuth.value) {
		links.push({ name: 'Войти', path: '/auth' });
	}
	
	return links;
});

onMounted(() => {
	isAuth.value = checkAuth();
	authUserName.value = getUserName();
});

const updateAuthState = () => {
	isAuth.value = checkAuth();
	authUserName.value = getUserName();
};

const logout = () => {
	localStorage.removeItem("authToken");
	localStorage.removeItem("authUserName");
	sessionStorage.removeItem("authToken");
	sessionStorage.removeItem("authUserName");
	isAuth.value = false;
	authUserName.value = '';
	router.push('/auth');
};

defineExpose({ updateAuthState });
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

        <div class="navbar-nav-item-user" v-if="isAuth">
            <a>{{ authUserName }}</a>
            <div class="dropdown-content">
                <a @click="logout">Выйти</a>
            </div>
        </div>
    </nav>
</div>
</template>

<style lang="less" scoped>
@import '../styles/style.less';

</style>