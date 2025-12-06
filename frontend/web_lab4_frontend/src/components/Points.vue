<template>
  <div>
    <h1>Точки</h1>
    <div class="grid-map-form">

      <Transition appear name="map-fade">
      <div>
        <canvas id="map-canvas" ref="mapCanvas" @click="canvasClick($event)" width="300" height="300"></canvas>
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
                    required
					maxlength="8" />
                </div>
                <div class="grid-form-entry">
                  <p class="form-label">Введите Y</p>
                  <input 
                    v-model="y" 
                    id="y-field" 
                    type="number" 
                    placeholder="Число (-5; 3)"
                    step="any"
                    required
					maxlength="8" />
                </div>
                <div class="grid-form-entry">
                  <p class="form-label">Введите R</p>
                  <input 
                    v-model="r" 
                    id="r-field" 
                    type="number" 
                    step="any"
                    placeholder="Число (0; 3)"
                    required
					maxlength="8" />
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
                  <p v-html="currentErrorMessage"></p>
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
                <p>{{ point.hit ? "Попал" : "Промазал" }}</p>
                <p>{{ point.x }}</p>
                <p>{{ point.y }}</p>
                <p>{{ point.r }}</p>
                <p>{{ point.user }}</p>
                <p>{{ point.date }}</p>
              </div>
            </TransitionGroup>
			
			<p class="card card-body" v-if="!isAuthenticated" style="text-align: center;">Тут ничего нет, потому что вы не авторизованы!</p>
            <p class="card card-body" v-else-if="points.length === 0" style="text-align: center;">Точек пока нет. Добавьте их!</p>
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
import axios from 'axios';
import api from '../services/api'
import { onMounted, ref, watch, onBeforeMount } from 'vue';
import { useRoute } from 'vue-router'
import { authService } from '../services/authService';

const isAuthenticated = ref(false);

const route = useRoute();

const x = ref(null);
const y = ref(null);
const r = ref(null);

const showErrorMessage = ref(false);
const currentErrorSummary = ref(null);
const currentErrorMessage = ref(null);
const timeoutId = ref(null);

const points = ref([]);

function updateAuthState() {
    isAuthenticated.value = authService.isAuthenticated();
}

onBeforeMount(() => {
    updateAuthState();
});

watch(() => route.path, () => {
    updateAuthState();
});


watch(r, (newR, oldR) => {
  const newRString = newR?.toString() || '';
  
  if (newRString.length > 8) {
    r.value = oldR;
    return;
  }
  
  if (validateR()[0] || !r.value) {
    refreshCanvas()
  }
})

watch(x, (newX, oldX) => {
  const newXString = newX?.toString() || ''

  if (newXString.length > 8) {
    x.value = oldX;
  }
})


watch(y, (newY, oldY) => {
  const newYString = newY?.toString() || ''

  if (newYString.length > 8) {
    y.value = oldY;
  }
})

const formatDate = (dateString) => {
	const date = new Date(dateString);
	const day = date.getDate().toString().padStart(2, '0');
	const month = (date.getMonth() + 1).toString().padStart(2, '0');
	const year = date.getFullYear();
	const hours = date.getHours().toString().padStart(2, '0');
	const minutes = date.getMinutes().toString().padStart(2, '0');
	
	return `${day}.${month}.${year} ${hours}:${minutes}`;
};

// init after mount
var one = null;
var centerX = null;
var centerY = null;
var canvas = null;
var ctx = null;
var width = null;
var height = null;

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
  if (timeoutId.value) clearTimeout(timeoutId.value);

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

async function submitPoint() {
	if (!isAuthenticated.value) {
		showError(
				"Ошибка авторизации", 
				"Вы не можете выполнить это действие. <a href='/auth'>Авторизуйтесь</a>"
			);
		return;
	} 

	if (!validateAndShowError()) return;

	try {
		const response = await api.post("/points", {
			x: x.value,
			y: y.value,
			r: r.value
		});

		const data = response.data;

		points.value.unshift(
			{
				"id": data.id,
				"hit": data.hit, 
				"x": x.value,
				"y": y.value,
				"r": r.value,
				"user": data.userId,
				"date": formatDate(data.createdAt)
			}
		)

		const absCoords = systemToAbsCoord(x.value, y.value);
		drawDot(absCoords.x, absCoords.y, data.hit)

		console.log(data)		
	} catch (error) {
		if (error.response) {
			switch (error.response.status) {
			case 400:
				showError("Bad Request", (await response.json()).error)
				break;
			case 401:
				showError("Ошибка авторизации", "Вы не можете выполнить это действие. <a href='/auth'>Авторизуйтесь</a>")
				
				break;		
			}
		}
		else {
			showError("Ошибка сети", error);

		}
	}
}

function drawCoordinateSystem() {
    const canvas = document.getElementById('map-canvas');
    const ctx = canvas.getContext('2d');

    const width = canvas.width;
    const height = canvas.height;

    console.log(width, height)

    const centerX = width / 2;
    const centerY = height / 2;
    const axisColor = '#333333';
    const arrowSize = 10;

    ctx.clearRect(0, 0, width, height);

    // оси координат
    ctx.strokeStyle = axisColor;
    ctx.lineWidth = 2;

    // ось х
    ctx.beginPath();
    ctx.moveTo(0, centerY);
    ctx.lineTo(width, centerY);
    ctx.stroke();

    // ось у
    ctx.beginPath();
    ctx.moveTo(centerX, 0);
    ctx.lineTo(centerX, height);
    ctx.stroke();

    // стрелка х
    ctx.beginPath();
    ctx.moveTo(width - arrowSize, centerY - arrowSize/2);
    ctx.lineTo(width, centerY);
    ctx.lineTo(width - arrowSize, centerY + arrowSize/2);
    ctx.stroke();

    // стрелка у
    ctx.beginPath();
    ctx.moveTo(centerX - arrowSize/2, arrowSize);
    ctx.lineTo(centerX, 0);
    ctx.lineTo(centerX + arrowSize/2, arrowSize);
    ctx.stroke();

    // и подписать курьером)
    ctx.fillStyle = axisColor;
    ctx.font = '14px Courier New';
    ctx.fillText('x', width - 15, centerY - 10);
    ctx.fillText('y', centerX + 10, 15);
    ctx.fillText('0', centerX + 5, centerY - 5);
}

// рисование точки по абсолютный координатам
function drawDot(x, y, success) {
    const color = success ? "#2f9051" : "#ef4444";
    const strokeColor = "black";
    const radius = 5;

    ctx.beginPath();
    ctx.arc(x, y, radius, 0, 2 * Math.PI);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = strokeColor;
    ctx.stroke();
}

// абсолютные координаты в системные
function absToSystemCoord(x, y) {
    const logicalX = (x - centerX) / one;
    const logicalY = (centerY - y) / one;

    return {x: logicalX.toFixed(2), y: logicalY.toFixed(3)};
}

// системные координаты в абсолютные
function systemToAbsCoord(x, y) {
    const absX = x * one + centerX;
    const absY = centerY - y * one;

    return {x: absX, y: absY};
}

function canvasClick(event) {
	if (!isAuthenticated.value) {
		showError(
				"Ошибка авторизации", 
				"Вы не можете выполнить это действие. <a href='/auth'>Авторизуйтесь</a>"
			);
		return;
	} 

	const rValid = validateR();
	if (!rValid[0]) {
		showError("Ошибка валидации", "Нельзя выбрать точку с некорректным R")
		return;
	}
	const rect = canvas.getBoundingClientRect();
	const clickX = event.clientX - rect.left;
	const clickY = event.clientY - rect.top;

	const systemCoords = absToSystemCoord(clickX, clickY);

	if (-5 >= systemCoords.y || 3 <= systemCoords.y) {
        showError("Неа (Y)", "Y должен быть от -5 до 3 не включительно");
        return;
    }
	if (-5 >= systemCoords.x || 3 <= systemCoords.x) {
        showError("Неа (X)", "X должен быть от -5 до 3 не включительно");
        return;
    }

	x.value = systemCoords.x;
	y.value = systemCoords.y;

	submitPoint();

}

async function getAllPoints() {
	try {
		const response = await api.get("/points")

		const pointsJson = response.data;
		pointsJson.forEach(point => {
			points.value.unshift(
				{
					"id": point.id,
					"hit": point.hit, 
					"x": point.x,
					"y": point.y,
					"r": point.r,
					"user": point.userId,
					"date": formatDate(point.createdAt)
				}
			)
		});
		
		
	} catch (error) {
		if (error.response) {
			switch(error.response.status) {
				case 400:
					showError("Bad Request", poinstJson.error)
					break;
				case 401:
					showError("Ошибка авторизации", "Вы не можете выполнить это действие. <a href='/auth'>Авторизуйтесь</a>")
					// await authService.logout()
					break;
			}
		}
		else {
			showError("Ошибка сети", error);
		}
	}
}

function refreshCanvasPoints() {
  points.value.forEach(point => {
      const absCoords = systemToAbsCoord(point.x, point.y);
      drawDot(absCoords.x, absCoords.y, point.hit)
  })
}
	

function refreshCanvas() {
	ctx.clearRect(0, 0, canvas.width, canvas.height);

	drawCoordinateSystem();
	drawFigure();
	refreshCanvasPoints();
}

function drawFigure() {
	if (validateR()[0] || r.value == null || r.value == '') {
		const rValue = r.value;

		ctx.fillStyle = "rgba(0, 48, 73, 0.7)";

		ctx.fillRect(centerX - rValue * one, centerY, rValue * one, rValue * one / 2)

		ctx.beginPath();
		ctx.moveTo(centerX, centerY);
		ctx.lineTo(centerX - rValue * one / 2, centerY);
		ctx.lineTo(centerX, centerY - rValue * one / 2);
		ctx.closePath();
		ctx.fill();

		ctx.beginPath();
		ctx.arc(centerX, centerY, rValue * one / 2, getRadians(0), getRadians(90));
		ctx.lineTo(centerX, centerY);
		ctx.closePath();
		ctx.fill();
	}
}

function getRadians(degrees) {
    return (Math.PI / 180) * degrees;
}

onMounted(() => {
	canvas = document.querySelector('#map-canvas');
	ctx = canvas.getContext('2d')
	width = canvas.width;
	height = canvas.height;
	centerX = width / 2;
	centerY = height / 2;
	one = 50;

	drawCoordinateSystem();

	if (!isAuthenticated.value) {
		canvas.style.filter = 'blur(5px)'
	} else {
		getAllPoints()
			.then(() => {
				refreshCanvasPoints()
			})
			.catch(error => {
				showError("Ошибка получения точек", error)
			})
	}

	
})
</script>
