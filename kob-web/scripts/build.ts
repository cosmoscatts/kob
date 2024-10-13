import path from 'node:path';
import dayjs from 'dayjs';
import fs from 'fs-extra';

async function updateLastUpdate() {
  const metaFilePath = path.resolve('./src/config/meta.ts');
  const lastUpdate = dayjs().format('YYYY-MM-DD');

  try {
    // 读取原文件内容
    let content = await fs.readFile(metaFilePath, 'utf-8');

    // 使用正则表达式更新 lastUpdate 字段
    content = content.replace(
      /(lastUpdate:\s*['"]).*?(['"])/,
      `$1${lastUpdate}$2`,
    );

    // 写入更新后的内容
    await fs.writeFile(metaFilePath, content, 'utf-8');

    console.log(`Successfully updated lastUpdate to ${lastUpdate}`);
  } catch (error) {
    console.error('Error updating lastUpdate:', error);
  }
}

updateLastUpdate();
