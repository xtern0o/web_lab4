<script setup>
import Header from './components/Header.vue';
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
  <div>
    <header>
      <div class="container">
        <Header ref="headerRef" />
      </div>
    </header>
    
    <main>
      <div class="container">
        <router-view />
      </div>
    </main>

    <footer>
      <div class="container">
        <p>footerfooterfooterfooterfooterfooterfooterfooterfooterfooter</p>
      </div>
    </footer>
  </div>
</template>

<style lang="less">
@import './styles/style.less';
</style>