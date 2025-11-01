<script setup>

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
            <router-link class="navbar-nav-item-link" to="/" exact-active-class="here">Главная</router-link>
            <router-link class="navbar-nav-item-link" to="/points" exact-active-class="here">Точки</router-link>
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
        <p>fdsfsdf</p>
      </div>
    </footer>
  </div>

</template>

<style lang="less">
@import './styles/style.less';

</style>

