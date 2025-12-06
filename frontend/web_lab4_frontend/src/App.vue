<script setup>
import Header from './components/Header.vue';
import { computed, onMounted } from 'vue';
import { useRouter } from 'vue-router'
import { authService } from './services/authService';

const router = useRouter();

const navigationLinks = computed(() => {
	const links = [
		{ name: 'Главная', path: '/' },
		{ name: 'Точки', path: '/points' },
	];

  if (!authService.isAuthenticated) {
    links.push({ name: 'Войти', path: '/auth' });
  }
	
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
        <p style="text-align: center; width: 100%; justify-content: center;">три&nbsp;<del>сопли</del>&nbsp;кита, на которых все держится</p>
        <div class="centerized-gallery">
          <img src="./assets/img/vue_im.png"></img>
          <img src="./assets/img/spring_im.png"></img>
          <img src="./assets/img/keycloak_im.png"></img>
        </div>
      </div>
    </footer>
  </div>
</template>

<style lang="less">
@import './styles/style.less';
@import './styles/variables.less';

.centerized-gallery {
  display: flex;
  flex-direction: row;
  gap: 20px;
  width: 100%;
  justify-content: center;
  align-items: center;
  
  * {
    height: 30px;
    width: auto;
    flex-shrink: 0;
    object-fit: contain;
  }

  @media(max-width: @width-phone) {
    flex-direction: column;
  }
}

</style>