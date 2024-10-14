export const AUTH_TABS = {
  ACCOUNT: 'account',
  PHONE: 'phone',
  REGISTER: 'register',
} as const;

export type AuthTab = typeof AUTH_TABS[keyof typeof AUTH_TABS];

export const SMS_CONSTANTS = {
  SEND_CODE_BTN_LABEL_DEFAULT: '发送验证码',
  RETRY_BTN_LABEL: '重新发送',
  COUNT_MAX_NUM: 60,
};
