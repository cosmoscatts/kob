import type { FormItemRule } from 'naive-ui';

/** 校验手机号 */
export const REGEXP_PHONE = /^1((3\d)|(4[014-9])|(5[0-35-9])|(6[2567])|(7[0-8])|(8\d)|(9[0-35-9]))\d{8}$/;

export function validatePhoneNumber(_rule: FormItemRule, value: string): boolean | Error {
  if (REGEXP_PHONE.test(value)) {
    return true;
  }
  return new Error('请输入正确的手机号');
}

export function validateUsername(_rule: FormItemRule, value: string): boolean | Error {
  if (value.length >= 1 && value.length <= 20) {
    return true;
  }
  return new Error('账号的长度为 1 ~ 20');
}
