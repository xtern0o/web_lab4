<template>

</template>
<script>
import { onBeforeMount } from 'vue';
import { authService } from '../services/authService'

var hasPermission = false;

onBeforeMount(() => {
    if (!authService.isAuthenticated) {
        hasPermission = false;
        return;
    }
    if (!authService.getUserInfo.roles.map(role => role.toUpperCase()).includes("ADMIN")) {
        hasPermission = false;
        return;
    }
    hasPermission = true;
})
</script>