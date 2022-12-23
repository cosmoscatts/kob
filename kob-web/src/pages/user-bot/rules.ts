export function createRules() {
  return {
    title: [
      {
        required: true,
        message: '请输入标题',
      },
    ],
    content: [
      {
        required: true,
        message: '请输入代码',
      },
    ],
  }
}
