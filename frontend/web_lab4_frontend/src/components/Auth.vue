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



</template>

<script setup>
import { ref } from 'vue'

const username = ref(null);
const password = ref(null);
const remember = ref(false);

const toggleAuth = ref(false);

function validateUsername() {
    let name = username.value.trim();

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


function validatePassword() {
    let pwd = password.value;

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

function validateAndThrow() {
    const validateUsername_ret = validateUsername();
    if (!validateUsername_ret[0]) {
        throwError("Невалидный никнейм", validateUsername_ret[1])
        return false;
    }
    const validatePassword_ret = validatePassword();
    if (!validatePassword_ret[0]) {
        throwError("Невалидный пароль", validatePassword_ret[1])
        return false;
    }
    return true;
}

function throwError(summary, message) {
    console.log(summary + " " + message)
}

function signin() {
    if (!validateAndThrow()) return;

    const data = {
        username: username.value,
        password: password.value,
        remember: remember.value
    }
    console.log(data);
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

</style>