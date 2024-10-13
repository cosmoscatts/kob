// ----- 游戏对象管理 -----

const GAME_OBJECTS: Set<Game> = new Set();

// ----- 游戏对象基类 -----

export class Game {
  timeDelta = 0; // 距离上一帧的时间间隔（ms）
  hasCalledStart = false;

  constructor() {
    GAME_OBJECTS.add(this);
  }

  start(): void {}

  update(): void {}

  beforeDestroy(): void {}

  destroy(): void {
    this.beforeDestroy();
    GAME_OBJECTS.delete(this);
  }
}

// ----- 游戏循环 -----

let lastTimestamp = 0;

const step = (timestamp: number): void => {
  const timeDelta = timestamp - lastTimestamp;

  for (const game of GAME_OBJECTS) {
    if (!game.hasCalledStart) {
      game.hasCalledStart = true;
      game.start();
    } else {
      game.timeDelta = timeDelta;
      game.update();
    }
  }

  lastTimestamp = timestamp;
  requestAnimationFrame(step);
};

requestAnimationFrame(step);
