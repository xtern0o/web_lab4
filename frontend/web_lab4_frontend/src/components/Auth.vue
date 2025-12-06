<template>

<h1>Авторизация</h1>


<div class="login-form">
    <Transition appear name="from-bottom">
        <button type="submit" class="button submit-button" v-on:click="gotoAuth">Войти с помощью Keycloak</button>
    </Transition>
</div>


<div class="login-form">
    <Transition appear name="error-fade">
        <div v-if="showErrorMessage" class="error-container" @click="closeError()">
            <img src="../assets/img/danger-18465_256.gif" />
            <div>
                <p class="error-container-header">{{ currentErrorSummary }}</p>
                <p v-html="currentErrorMessage"></p>
            </div>
        </div>
    </Transition>
</div>

</template>

<script setup>
import { onBeforeMount, ref } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '../services/authService'


const router = useRouter();

const showErrorMessage = ref(false);
const currentErrorSummary = ref(null);
const currentErrorMessage = ref(null);
const timeoutId = ref(null);

function closeError() {
    showErrorMessage.value = false;
    currentErrorSummary.value = null;
    currentErrorMessage.message = null;

    if (timeoutId.value) {
        clearTimeout(timeoutId.value);
        timeoutId.value = null;
    }
}

function showError(summary, message) {
    if (timeoutId.value) {
        clearTimeout(timeoutId.value);
    }

    showErrorMessage.value = true;
    currentErrorSummary.value = summary;
    currentErrorMessage.value = message;
    timeoutId.value = setTimeout(closeError, 5000);
}

function gotoAuth() {
    try {
        authService.login();
    } catch (err) {
        showError("Ошибка перенаправления", err)
    }
}

onBeforeMount(() => {
    if (authService.isAuthenticated()) {
        router.push('/points');
    }
})


</script>

<style lang="less" scoped>
@import '../styles/style.less';
@import '../styles/variables.less';

.login-form {
  display: flex;
  flex-direction: column;
  max-width: 600px;
  margin: 0 auto;
  padding: 1rem 1rem;
  width: 100%;
  gap: 1rem;

  p {
    padding: 0;
  }

  &-entry {
    display: flex;
    flex-direction: column;
    gap: 0.25rem;
  }
}

.checkbox-container {
    display: flex;
    align-items: center;
    justify-content: center;
    align-content: center
}

.remember-grid {
    .grid-proportions-adaptive(~"1fr 2.5fr");

    @media(max-width: @width-phone) {
        flex-direction: column-reverse;
        justify-items: center;
        align-items: center;

        button, div {
            width: 100%;
        }
    }
}

.from-top-enter-active, 
.from-top-leave-active,
.from-bottom-enter-active,
.from-bottom-leave-active {
  transition: all 0.8s ease;
}

.from-top-enter-from,
.from-top-leave-to {
  opacity: 0;
  @media(min-width: @width-phone) {
    transform: translateY(-50px);
  }
  
}

.from-bottom-enter-from,
.from-bottom-leave-to {
  opacity: 0;
  @media(min-width: @width-phone) {
    transform: translateY(50px);
  }
  
}

.error-fade-enter-active,
.error-fade-leave-active {
  transition: all 0.4s ease;
}

.error-fade-enter-from,
.error-fade-leave-to {
  opacity: 0;
  transform: translateY(-40px);
}

</style>