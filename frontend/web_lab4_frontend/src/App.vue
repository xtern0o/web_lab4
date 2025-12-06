<script setup>
import Header from './components/Header.vue';
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router'

const router = useRouter();

const navigationLinks = computed(() => {
	const links = [
		{ name: 'Главная', path: '/' },
		{ name: 'Точки', path: '/points' },
	];
	
	links.push({ name: 'Войти', path: '/auth' });
	
	return links;
});

onMounted(() => {

});

const updateAuthState = () => {

};

const logout = () => {
	

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
        <p>some footer info</p>
      </div>
    </footer>
  </div>
</template>

<style lang="less">
@import './styles/style.less';
</style>