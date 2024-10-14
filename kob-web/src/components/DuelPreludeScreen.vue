<script setup lang="ts">
import defaultAvatar from '~/assets/default-avatar.png';

const { user } = storeToRefs(useUserStore());
const { opponent, players } = storeToRefs(usePkStore());

const bodyColor = computed(() => isDark.value ? '#121212' : '#FFFFFF');

// 控制加载动画
const { loading, endLoading } = useLoading(true);
useTimeoutFn(endLoading, 5000);

// VS 倒计时逻辑
const title = ref<string | number>('VS');
const { pause, resume } = useIntervalFn(() => {
  if (title.value === 0) {
    pause();
    title.value = 'VS';
    return;
  }
  title.value = title.value === 'VS' ? 3 : (title.value as number) - 1;
}, 1000);
useTimeoutFn(resume, 500);

// 计算 VS 标题位置
const titleLeft = computed(() =>
  title.value === 'VS' ? 'calc(50% - 10rem)' : 'calc(50% - 5rem)',
);

// 获取玩家头像
const getPlayerAvatar = (index: number) =>
  (players.value?.[index]?.id === user.value?.id ? user.value?.avatar : opponent.value?.avatar) || defaultAvatar;

// 获取玩家名称
const getPlayerName = (index: number) =>
  players.value?.[index]?.id === user.value?.id ? user.value?.name : opponent.value?.name;

function onBeforeLeave(el: Element) {
  el.classList.add('leaving');
}

function onAfterLeave(el: Element) {
  el.classList.remove('leaving');
}
</script>

<template>
  <div class="loading-mask" />
  <Transition
    leave-active-class="animate__animated animate__fadeOut"
    @before-leave="onBeforeLeave"
    @after-leave="onAfterLeave"
  >
    <div v-if="loading" class="loading-wrapper">
      <div class="side left animate__animated animate__fadeInLeftBig">
        <div class="avatar-container">
          <img :src="getPlayerAvatar(0)" alt="Player 1" class="avatar">
          <div class="avatar-decoration">
            <div class="avatar-glow" />
          </div>
        </div>
        <div class="player-name">
          {{ getPlayerName(0) || '匿名玩家1号' }}
        </div>
      </div>
      <div class="vs-container animate__animated animate__fadeInDownBig animate__faster">
        <div class="vs font-game">
          {{ title }}
        </div>
        <div class="vs-glow" />
      </div>
      <div class="side right animate__animated animate__fadeInRightBig">
        <div class="avatar-container">
          <img :src="getPlayerAvatar(1)" alt="Player 2" class="avatar">
          <div class="avatar-decoration">
            <div class="avatar-glow" />
          </div>
        </div>
        <div class="player-name">
          {{ getPlayerName(1) || '匿名玩家2号' }}
        </div>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.loading-mask {
  position: fixed;
  inset: 0;
  background-color: v-bind('bodyColor');
  pointer-events: none;
  z-index: 10000;
}

.loading-wrapper {
  position: fixed;
  inset: -300px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.5);
  z-index: 10001;
  transition: opacity 0.5s ease-out;
}

.loading-wrapper.leaving {
  opacity: 0;
}

.side {
  width: 50%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
}

.side.left {
  padding-left: 300px;
}

.side.right {
  padding-right: 300px;
}

.side:before {
  content: '';
  position: absolute;
  inset: 0;
  z-index: -1;
  transform: skew(.08turn);
  box-shadow: 0px 0px 20px #FFCD87 inset;
}

.side.left:before {
  background: linear-gradient(#6AA1FF, #3C108F);
}

.side.right:before {
  background: linear-gradient(#CB272D, #FAAC7B);
}

.avatar-container {
  width: 250px;
  height: 250px;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: visible;
}

.avatar {
  width: 220px;
  height: 220px;
  border-radius: 50%;
  object-fit: cover;
  z-index: 2;
  transition: all 0.3s ease;
  box-shadow: 0 0 20px rgba(255, 255, 255, 0.5);
  cursor: pointer;
}

.avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 0 30px rgba(255, 255, 255, 0.8);
}

.avatar-decoration {
  position: absolute;
  width: 250px;
  height: 250px;
  border-radius: 50%;
  z-index: 1;
  animation: rotate 10s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.side.left .avatar-decoration {
  background: radial-gradient(circle, rgba(0,0,255,0.2) 0%, rgba(0,0,255,0.5) 50%, rgba(0,0,255,0.8) 100%);
  box-shadow: 0 0 20px blue, inset 0 0 20px blue;
}

.side.right .avatar-decoration {
  background: radial-gradient(circle, rgba(255,0,0,0.2) 0%, rgba(255,0,0,0.5) 50%, rgba(255,0,0,0.8) 100%);
  box-shadow: 0 0 20px red, inset 0 0 20px red;
}

.avatar-glow {
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255,255,255,0.8) 0%, rgba(255,255,255,0) 70%);
  opacity: 0;
  animation: glow 2s ease-in-out infinite alternate;
}

@keyframes glow {
  from { opacity: 0; }
  to { opacity: 0.5; }
}

.vs-container {
  position: absolute;
  left: v-bind(titleLeft);
  top: calc(50% - 12rem);
  z-index: 1000;
}

.vs {
  font-size: 14rem;
  font-style: italic;
  font-weight: bold;
  text-align: center;
  color: #FF9626;
  text-shadow: 0 0 0.6rem #FFE3B8, 0 0 1.5rem #FFCD87,
    -0.2rem 0.1rem 1rem #FFCD87, 0.2rem 0.1rem 1rem #FFCD87,
    0 -0.5rem 2rem #D26913, 0 0.5rem 3rem #D26913;
  animation: vs-pulse 1.5s ease-in-out infinite alternate;
}

@keyframes vs-pulse {
  from { transform: scale(1); filter: brightness(1); }
  to { transform: scale(1.1); filter: brightness(1.2); }
}

.vs-glow {
  position: absolute;
  inset: -20px;
  background: radial-gradient(circle, rgba(255,150,38,0.4) 0%, rgba(255,150,38,0) 70%);
  filter: blur(15px);
  opacity: 0;
  animation: vs-glow 2s ease-in-out infinite alternate;
}

@keyframes vs-glow {
  from { opacity: 0; transform: scale(0.9); }
  to { opacity: 0.6; transform: scale(1.1); }
}

.player-name {
  margin-top: 20px;
  font-size: 2rem;
  font-weight: bold;
  color: #fff;
  text-shadow: 0 0 10px rgba(255, 255, 255, 0.8);
  animation: name-glow 2s ease-in-out infinite alternate;
}

@keyframes name-glow {
  from { text-shadow: 0 0 10px rgba(255, 255, 255, 0.8); }
  to { text-shadow: 0 0 20px rgba(255, 255, 255, 1); }
}

.animate__fadeOut {
  animation-duration: 0.5s;
}
</style>
