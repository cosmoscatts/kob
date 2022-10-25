# KOB Acapp

## 上传 `Acapp` 注意点
 - 打包后需要将 assets 文件夹内所有内容传至服务器
 - 将 `js` 文件命名为 `app.js`
 - 将 `css` 文件命名为 `app.css`
 - 在 `app.js` 末尾添加
    ```
    // 将 `Mp(cD).use(k0).mount(id);` 替换为下面内容
    var AcWingOS;
    const init = (id, _AcWingOS) => {
      AcWingOS = _AcWingOS;
      Ap(aD).use(x0).mount(id);
    }
    export class Game {
        constructor(id, AcWingOS) {
          const myappid = '#' + id;
          init(myappid, AcWingOS);
        }
    }
    ```
