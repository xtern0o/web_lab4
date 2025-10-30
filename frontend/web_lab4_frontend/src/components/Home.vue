<template>
    <h1>Главная</h1>
    <div class="grid-clock-info">
        
        <canvas id="clock-canvas" ref="canvasRef"></canvas>
                
        <div class="card">
            <p class="card-header">Че тут есть</p>
            <div class="card-body">
                
               
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';

const canvasRef = ref(null);
let intervalId = null;

const clockBgColor = '#1e1e1e'
const clockTicksColor = '#888'

function drawClock(ctx, radius, canvas) {
  canvas.width = radius * 2;
  canvas.height = radius * 2;
  canvas.style.width = (radius * 2) + 'px';
  canvas.style.height = (radius * 2) + 'px';

  ctx.clearRect(0, 0, canvas.width, canvas.height);

  ctx.save();
  ctx.translate(radius, radius);

  drawFace(ctx, radius);
  drawTicks(ctx, radius);
  drawTime(ctx, radius);

  ctx.restore();
}

function drawFace(ctx, radius) {
  ctx.beginPath();
  ctx.arc(0, 0, radius, 0, 2 * Math.PI);
  ctx.fillStyle = clockBgColor;
  ctx.fill();

  ctx.strokeStyle = clockTicksColor;
  ctx.lineWidth = radius * 0.03;
  ctx.stroke();
}

function drawTicks(ctx, radius) {
  ctx.strokeStyle = clockTicksColor;
  for (let i = 0; i < 60; i++) {
    const angle = (i / 60) * 2 * Math.PI;
    ctx.save();
    ctx.rotate(angle);

    ctx.beginPath();
    if (i % 5 === 0) {
      ctx.lineWidth = radius * 0.04;
      ctx.moveTo(0, -radius * 0.9);
    } else {
      ctx.lineWidth = radius * 0.015;
      ctx.moveTo(0, -radius * 0.95);
    }

    ctx.lineTo(0, -radius);
    ctx.stroke();

    ctx.restore();
  }
}

function drawTime(ctx, radius) {
  const now = new Date();
  let hour = now.getHours();
  let minute = now.getMinutes();
  let second = now.getSeconds();
  hour %= 12;

  const hourAngle = (hour * Math.PI / 6) +
    (minute * Math.PI / (6 * 60)) +
    (second * Math.PI / (360 * 60));

  const minuteAngle = (minute * Math.PI / 30) + (second * Math.PI / (30 * 60));
  const secondAngle = (second * Math.PI / 30);

  drawHand(ctx, hourAngle, radius * 0.5, Math.max(3, radius * 0.06), '#111');
  drawHand(ctx, minuteAngle, radius * 0.75, Math.max(2, radius * 0.04), '#111');
  drawHand(ctx, secondAngle, radius * 0.9, Math.max(1, radius * 0.015), '#b00000');
}

function drawHand(ctx, pos, length, width, color) {
  ctx.beginPath();
  ctx.lineWidth = width;
  ctx.lineCap = "round";
  ctx.strokeStyle = color;

  ctx.moveTo(0, 0);
  ctx.rotate(pos);
  ctx.lineTo(0, -length);
  ctx.stroke();
  ctx.rotate(-pos);
}

onMounted(() => {
  const canvas = canvasRef.value;
  if (!canvas) return;
  const ctx = canvas.getContext('2d');

  const radius = canvas.width / 2;

  drawClock(ctx, radius, canvas);

  intervalId = setInterval(() => {
    drawClock(ctx, radius, canvas);
  }, 1000);
});

onBeforeUnmount(() => {
  if (intervalId) clearInterval(intervalId);
});
</script>

