<script setup>
import { ref } from 'vue';


const navLinks = ref([
  { name: 'Главная', path: '/' },
  { name: 'Точки', path: '/points' },
  { name: 'Войти', path: '/auth' },
]);
</script>

<template>

  <div>
    <header>
      <div class="container">
        <div class="navbar">
          <div class="navbar-brand">
            <p class="navbar-brand-link">P3211</p>
            <p class="navbar-brand-link">Карнажицкий Максим Романович</p>
            <p class="navbar-brand-link">Лаб 4</p>
            <p class="navbar-brand-link">8281</p>
          </div>

          <nav class="navbar-nav">
            <router-link
              v-for="link in navLinks"
              :key="link.path"
              class="navbar-nav-item-link"
              :to="link.path"
              exact-active-class="here"
            >
              {{ link.name }}
            </router-link>
          </nav>
          
        </div>
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

