import { SMS_CONSTANTS } from './constants';

/**
 * 发送验证码后倒计时功能
 */
export function useCountdownTimer() {
  const isCounting = ref(false);
  const sendCodeBtnLabel = ref(SMS_CONSTANTS.SEND_CODE_BTN_LABEL_DEFAULT);
  let maxMs = SMS_CONSTANTS.COUNT_MAX_NUM;

  const { resume, pause } = useIntervalFn(() => {
    if (maxMs > 0) {
      maxMs -= 1;
      sendCodeBtnLabel.value = `${maxMs} 秒后重新获取`;
    } else {
      isCounting.value = false;
      sendCodeBtnLabel.value = SMS_CONSTANTS.RETRY_BTN_LABEL;
      pause();
    }
  }, 1000, { immediate: false });

  const startCounting = () => {
    isCounting.value = true;
    maxMs = SMS_CONSTANTS.COUNT_MAX_NUM;
    resume();
  };

  return {
    isCounting,
    sendCodeBtnLabel,
    startCounting,
  };
}
