import antfu from '@antfu/eslint-config';

export default antfu({
  rules: {
    'style/semi': ['error', 'always'],
    'style/brace-style': 'off',
    'antfu/top-level-function': 'off',
    'ts/no-unsafe-function-type': 'off',
  },
});
