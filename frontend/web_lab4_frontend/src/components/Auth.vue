<template>

<h1>
    <template v-if="toggleAuth">Регистрация</template>
    <template v-else>Авторизация</template>
</h1>

<Transition appear name="from-top">
<div class="login-form">
    <button type="button" @click="toggleAuth = !toggleAuth">
        <template v-if="toggleAuth">Есть аккаунт?</template>
        <template v-else>Нет аккаунта?</template>
    </button>
</div>
</Transition>

<form @submit.prevent="signup()" v-if="toggleAuth">
    <div class="login-form">
        <Transition appear name="from-top">
            <div class="grid-form-entry">
                <p>Никнейм</p>
                <input 
                    v-model="registerUsername"
                    type="text"
                    id="register-username-field"
                    placeholder="От 3 символов..."
                    required>
            </div>
        </Transition>
        <Transition appear name="from-top">
            <div class="grid-form-entry">
                <p>Пароль</p>
                <input
                    v-model="registerPassword"
                    type="password"
                    id="register-password-field"
                    placeholder="От 6 символов..."
                    required>
            </div>
        </Transition>   
        <Transition appear name="from-top">
            <div>
                <input
                    v-model="registerPasswordRepeat"
                    type="password"
                    id="register-password-repeat-field"
                    placeholder="Повторите пароль"
                    required>
            </div>
        </Transition>  
        <Transition appear name="from-bottom">
            <div class="remember-grid">
                <div class="checkbox-container">
                    <input 
                        type="checkbox" 
                        id="register-rememberCheck"
                        v-model="remember">
                        <label for="rememberCheck">Запомнить</label>
                    </input>
                </div>
                <button type="submit" class="submit-button" >Зарегистрироваться</button>
            </div>
        </Transition>
    </div>
</form>


<form @submit.prevent="signin()" v-else>
    <div class="login-form">
        <Transition appear name="from-top">
            <div class="grid-form-entry">
                <p>Никнейм</p>
                <input 
                    v-model="username"
                    type="text"
                    id="login-username-field"
                    placeholder="От 3 символов..."
                    required>
            </div>
        </Transition>
        <Transition appear name="from-top">
            <div class="grid-form-entry">
                <p>Пароль</p>
                <input
                    v-model="password"
                    type="password"
                    id="login-password-field"
                    placeholder="От 6 символов..."
                    required>
            </div>
        </Transition>   
        <Transition appear name="from-bottom">
            <div class="remember-grid">
                <div class="checkbox-container">
                    <input 
                        type="checkbox" 
                        id="login-rememberCheck"
                        v-model="remember">
                        <label for="rememberCheck">Запомнить</label>
                    </input>
                </div>
                <button type="submit" class="submit-button" >Войти</button>
            </div>
        </Transition>
    </div>
</form>

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

const showErrorMessage = ref(false);
const currentErrorSummary = ref(null);
const currentErrorMessage = ref(null);
const timeoutId = ref(null);

const username = ref(null);
const password = ref(null);
const remember = ref(false);

const registerUsername = ref(null);
const registerPassword = ref(null);
const registerPasswordRepeat = ref(null);

const toggleAuth = ref(false);



onBeforeMount(() => {
    const checkAuth = () => {
        const hasLocalToken = localStorage.getItem("authToken") !== null;
        const hasSessionToken = sessionStorage.getItem("authToken") !== null;
        return hasLocalToken || hasSessionToken;
    };
    if (checkAuth()) {
        router.push("/points")
    }
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

function validateUsername(nam) {
    let name = nam.trim();

    if (!name) {
        return [false, 'Имя пользователя не может быть пустым.'];
    }
    if (name.length < 3) {
        return [false, 'Имя пользователя должно содержать не менее 3 символов.'];
    }
    if (name.length > 20) {
        return [false, 'Имя пользователя должно содержать не более 20 символов.'];
    }
    const usernameRegex = /^[a-zA-Z0-9_]+$/;
    if (!usernameRegex.test(name)) {
        return [false, 'Имя пользователя может содержать только латинские буквы, цифры и подчеркивания.'];
    }

    return [true, "OK"];
}


function validatePassword(pwd) {
    if (!pwd) {
        return [false, 'Пароль не может быть пустым.'];
    }
    if (pwd.length < 6) {
        return [false, 'Пароль должен содержать не менее 6 символов.'];
    }
    if (pwd.length > 30) {
        return [false, 'Пароль должен содержать не более 30 символов.'];
    }
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{6,}$/;
    if (!passwordRegex.test(pwd)) {
        return [false, 'Пароль должен содержать хотя бы одну латинскую заглавную букву, одну латинскую строчную букву и одну цифру.'];
    }

    return [true, "OK"];
}

function validateAndThrow(name, pwd) {
    const validateUsername_ret = validateUsername(name);
    if (!validateUsername_ret[0]) {
        showError("Невалидный никнейм", validateUsername_ret[1])
        return false;
    }
    const validatePassword_ret = validatePassword(pwd);
    if (!validatePassword_ret[0]) {
        showError("Невалидный пароль", validatePassword_ret[1])
        return false;
    }
    return true;
}

async function signin() {
    if (localStorage.getItem("authToken") !== null) {
        showError("Ошибка авторизации", "Вы уже авторизованы")
        return;
    }

    if (!validateAndThrow(username.value, password.value)) return;

    let name = username.value;
    let pwd = password.value;

    const data = {
        name: name,
        password: pwd
    }

    try {
        const response = await fetch(
            apiConfig.apiUrl + '/auth/signin',
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
                case 404:
                    showError("404 Not Found", "Пользователя с таким именем не найдено")
                    break;
                case 400:
                    showError("400 Bad Request", "Невалидные данные")
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