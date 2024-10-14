import { appMeta } from '~/config';

/**
 * 获取验证码
 */
export function getSmsCode() {
  useTimeoutFn(() => {
    $notification.info({
      content: `【${appMeta.shortName}】验证码：123456，有效期10分钟。如非本人操作，请忽略。`,
      duration: 30 * 1000,
    });
  }, 5000);
}
