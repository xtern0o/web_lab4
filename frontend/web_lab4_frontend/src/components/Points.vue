<template>
  <div>
    <h1>Точки</h1>
    <div class="grid-map-form">

      <Transition appear name="map-fade">
      <div>
        <canvas id="map-canvas" ref="mapCanvas" @click="canvasClick($event)"></canvas>
      </div>
      </Transition>

      <Transition appear name="form-fade">
      <div class="card">
          <h3 class="card-header">Добавить точку</h3>
          <div class="card-body">

            <form @submit.prevent="submitPoint">

              <div class="grid-form">
                <div class="grid-form-entry">
                  <p class="form-label">Введите X</p>
                  <input 
                    v-model="x" 
                    id="x-field" 
                    type="number" 
                    placeholder="Число (-5; 3)"
                    step="any"
                    required />
                </div>
                <div class="grid-form-entry">
                  <p class="form-label">Введите Y</p>
                  <input 
                    v-model="y" 
                    id="y-field" 
                    type="number" 
                    placeholder="Число (-5; 3)"
                    step="any"
                    required />
                </div>
                <div class="grid-form-entry">
                  <p class="form-label">Введите R</p>
                  <input 
                    v-model="r" 
                    id="r-field" 
                    type="number" 
                    step="any"
                    placeholder="Число (0; 3)"
                    required />
                </div>
              </div>

            <div class="grid-buttons">
              <button type="button" @click="clearFields()">Очистить</button>
              <button type="submit" class="submit-button">Отправить</button>
            </div>

            </form>
            
            <Transition name="error-fade">
              <div v-if="showErrorMessage" class="error-container" @click="closeError()">
              
                <img src="../assets/img/danger-18465_256.gif" />
                <div>
                  <p class="error-container-header">{{ currentErrorSummary }}</p>
                  <p>{{ currentErrorMessage }}</p>
                </div>
              
              </div>
            </Transition>
            

          </div>
      </div>
      </Transition>  
    </div>

    <Transition appear name="data-fade">
      <div class="card card-body">
        <div class="points-data">
          <div class="point-entry">
            <p>Hit?</p>
            <p>X</p>
            <p>Y</p>
            <p>R</p>
            <p>Пользователь</p>
            <p>Дата</p> 
          </div>

            <TransitionGroup name="form-fade" tag="div">
              <div v-for="point in points" class="point-entry" :key="point.id">
                <p>{{ point.hit }}</p>
                <p>{{ point.x }}</p>
                <p>{{ point.y }}</p>
                <p>{{ point.r }}</p>
                <p>{{ point.user }}</p>
                <p>{{ point.date }}</p>
              </div>
            </TransitionGroup>
          
            <p class="card card-body" v-if="points.length === 0" style="text-align: center;">Точек пока нет. Добавьте их!</p>

        </div>
      </div>
    </Transition>
    
  </div>
</template>

<style lang="less" scoped>
@import '../styles/style.less';
@import '../styles/variables.less';

.grid-form {
  .grid-proportions-adaptive(~"1fr 1fr 1fr", 1rem);
  margin: 0 0;
  width: 100%;

  &-entry {
    display: flex;
    flex-direction: column;
    gap: 0.25rem;
  }
}

.grid-buttons {
  .grid-proportions(~"1fr 1fr", 1rem);
  margin: 1rem 0;
}

.form-fade-enter-active, 
.form-fade-leave-active,
.map-fade-enter-active,
.map-fade-leave-active,
.data-fade-enter-active,
.data-fade-leave-active {
  transition: all 0.8s ease;
}

.error-fade-enter-active,
.error-fade-leave-active {
  transition: all 0.4s ease;
}

.form-fade-enter-from,
.form-fade-leave-to {
  opacity: 0;
  @media(min-width: @width-phone) {
    transform: translateX(50px);
  }
  
}

.map-fade-enter-from,
.map-fade-leave-to {
  opacity: 0;
  @media(min-width: @width-phone) {
    transform: translateX(-50px);
  }
}

.error-fade-enter-from,
.error-fade-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

.data-fade-enter-from,
.data-fade-leave-to {
  opacity: 0;
  transform: translateY(50px);
}

</style>

<script setup>
import { ref } from 'vue';

const x = ref(null);
const y = ref(null);
const r = ref(null);

const showErrorMessage = ref(false);
const currentErrorSummary = ref(null);
const currentErrorMessage = ref(null);

const timeoutId = ref(null);

const points = ref([]);

function closeError() {
  showErrorMessage.value = false;
  currentErrorSummary.value = null;
  currentErrorMessage.message = null;

  if (timeoutId.value) {
    clearTimeout(timeoutId.value);
    timeoutId.value = null;
  }
}

function validateX() {
  if (x.value === null) return [false, "X не должен быть пустым"]
  const xVal = parseFloat(x.value);
  if (isNaN(xVal)) return [false, "X должен быть числом"]
  if (x.value <= -5 || x.value >= 3) return [false, "X должен быть от -5 до 3 не включительно"]
  return [true, "OK"]
}

function validateY() {
  if (y.value === null) return [false, "Y не должен быть пустым"]
  const yVal = parseFloat(y.value);
  if (isNaN(yVal)) return [false, "Y должен быть числом"]
  if (y.value <= -5 || y.value >= 3) return [false, "Y должен быть от -5 до 3 не включительно"]
  return [true, "OK"]
}

function validateR() {
  if (r.value === null) return [false, "R не должен быть пустым"]
  const rVal = parseFloat(r.value);
  if (isNaN(rVal)) return [false, "R должен быть числом"]
  if (r.value <= 0 || r.value >= 3) return [false, "R должен быть от 0 до 3 не включительно"]
  return [true, "OK"]
}

function showError(summary, message) {
  if (timeoutId.value) {
    clearTimeout(timeoutId.value);
  }

  showErrorMessage.value = true;
  currentErrorSummary.value = summary;
  currentErrorMessage.value = message;
  timeoutId.value = setTimeout(closeError, 3000);
  
}

function validateAndShowError() {
  console.log("in valida")
  const xValid = validateX();
  if (!xValid[0]) {
    showError("Ошибка валидации", xValid[1]);
    return false;
  }

  const yValid = validateY();
  if (!yValid[0]) {
    showError("Ошибка валидации", yValid[1]);
    return false;
  }

  const rValid = validateR();
  if (!rValid[0]) {
    showError("Ошибка валидации", rValid[1]);
    return false;
  }
  
  return true;

}

function clearFields() {
  x.value = null;
  y.value = null;
  r.value = null;
}

var k = 0;

function submitPoint() {
  if (!validateAndShowError()) return;

  let pointDto = {
    "x": x.value,
    "y": y.value,
    "r": r.value
  }

  points.value.unshift(
    {
      "id": ++k,
      "hit": "ye", 
      "x": pointDto.x,
      "y": pointDto.y,
      "r": pointDto.r,
      "user": "maxkarn",
      "date": "Nov 2 2025 12:51"
    }
  )

  console.log(pointDto);
}

function canvasClick(event) {
  console.log(event)
}

</script>
