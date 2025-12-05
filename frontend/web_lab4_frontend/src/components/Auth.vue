<template>

<h1>Авторизация</h1>


<div class="login-form">
    <Transition appear name="from-bottom">
        <a type="submit" class="button submit-button" :href="keycloakAuthUri">Войти с помощью Keycloak</a>
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
import { inject, onBeforeMount, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'


const router = useRouter();

const apiConfig = inject('apiConfig');

const apiUrl = apiConfig.apiUrl;

const showErrorMessage = ref(false);
const currentErrorSummary = ref(null);
const currentErrorMessage = ref(null);
const timeoutId = ref(null);

const authConfig = ref(null);
const keycloakAuthUri = ref(null);


onBeforeMount(() => {
    const checkAuth = () => {
        const hasLocalToken = localStorage.getItem("authToken") !== null;
        const hasSessionToken = sessionStorage.getItem("authToken") !== null;
        return hasLocalToken || hasSessionToken;
    };
    if (checkAuth()) {
        router.push("/points")
    }
    initAuthConfig();

    console.log(apiConfig)
})

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

async function initAuthConfig() {
    try {
        const response = await fetch(apiUrl + "/auth/config");
        if (! response.ok) {
            showError(response.status + " " + response.statusText, response.body)
            return;
        }
        const data = await response.json();
        authConfig.value = data;

        const url = new URL(`${data.auth_url}/realms/${data.realm}/protocol/openid-connect/auth`)
        url.searchParams.append('client_id', data.client_id)
        url.searchParams.append('redirect_uri', `${window.location.origin}/callback`)
        url.searchParams.append('response_type', "code")
        url.searchParams.append('scope', "openid")

        keycloakAuthUri.value = url.toString();
        
    } catch (error) {
        showError("Ошибка сети", error)
    }
}

async function authViaKeycloak() {
    if (localStorage.getItem("authToken") !== null) {
        showError("Ошибка авторизации", "Вы уже авторизованы")
        return;
    }

    if (!validateAndThrow(username.value, password.value)) return;

    const name = username.value;
    const pwd = password.value;

    const data = {
        name: name,
        password: pwd
    }

    
    
    // TODO: тут реализовать kk oauth
}

async function signup() {
    if (localStorage.getItem("authToken") !== null) {
        showError("Ошибка авторизации", "Вы уже авторизованы")
        return;
    }

    let name = registerUsername.value;
    let pwd = registerPassword.value;
    let repeatPwd = registerPasswordRepeat.value;

    if (!(pwd === repeatPwd)) {
        showError("Некорректный пароль", "Пароли не совпадают")
        return;
    }

    if (!validateAndThrow(name, pwd)) return;

    const data = {
        name: name,
        password: pwd
    }

    try {
        const response = await fetch(
            apiConfig.apiUrl + '/auth/signup',
            {
                'method': 'POST',
                'headers': {
                    'Content-Type': 'application/json'
                },
                'body': JSON.stringify(data)
            }
        )
        

        if (response.ok) {
            const responseJson = await response.json();

            if (remember.value) {
                localStorage.setItem("authToken", responseJson.token)
                localStorage.setItem("authUserName", responseJson.username)
                localStorage.setItem("authUserId", responseJson.userId)
            } else {
                sessionStorage.setItem("authToken", responseJson.token)
                sessionStorage.setItem("authUserName", responseJson.username)
                sessionStorage.setItem("authUserId", responseJson.userId)
            }
            
            location.reload();
        }
        else {
            switch (response.status) {
                case 409:
                    showError("409 Conflict", "Пользователь с таким именем уже существует")
                    break;
                case 400:
                    showError("400 Bad Request", "Не нужно посылать плохие запросы")
                    break;
                case 302:
                    showError("302 Found", "Вы УЖЕ авторизованы")
                    break;
            }
        }
    } catch (error) {
        showError("Ошибка сети", error)
    }
}


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