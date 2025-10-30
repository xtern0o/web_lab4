<script setup>

import Home from './components/Home.vue';


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
            <a class="navbar-nav-item-link" href="google.com">Главная</a>
            <a class="navbar-nav-item-link" href="google.com">Точки</a>
          </nav>
          
        </div>
      </div>
    </header>
    
    <main>
      <div class="container">
        <Home />
      </div>
    </main>

    <footer>
      <div class="container">

      </div>
    </footer>
  </div>

</template>

<style lang="less">
@import './styles/style.less';

</style>

