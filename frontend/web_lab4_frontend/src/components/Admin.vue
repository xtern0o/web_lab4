<template>

<h1>Админка</h1>
<p>{{ hasPermission ? "Да" : "Нет" }}</p>

</template>
<script setup>
import { onBeforeMount, ref } from 'vue';
import { authService } from '../services/authService'

const hasPermission = ref(false);

onBeforeMount(() => {
    hasPermission.value = authService.isAdmin();
})
</script>