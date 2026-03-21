package com.kob.game.bot;

import com.kob.game.GameEngine;
import lombok.extern.slf4j.Slf4j;
import org.joor.Reflect;

import java.io.File;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Bot 代码编译执行器
 * 包含三层安全防护：源码静态检查 + SecurityManager 沙箱 + 独立 input 文件
 */
@Slf4j
public class BotExecutor extends Thread {
    private BotTask botTask;

    public void startTimeout(long timeout, BotTask botTask) {
        this.botTask = botTask;
        this.start();
        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            log.error("BotExecutor interrupted during timeout join", e);
        } finally {
            this.interrupt();
        }
    }

    /**
     * 在 code 中的 Bot 类名后面添加 uid
     */
    private String addUid(String code, String uid) {
        int k = code.indexOf(" implements java.util.function.Supplier<Integer>");
        return code.substring(0, k) + uid + code.substring(k);
    }

    /**
     * 替换 bot 代码中的 input.txt 为独立文件名
     */
    private String replaceInputFileName(String code, String inputFileName) {
        return code.replace("\"input.txt\"", "\"" + inputFileName + "\"");
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);
        String inputFileName = "input-" + uid + ".txt";
        File inputFile = new File(inputFileName);

        try {
            // Layer 1: 源码静态检查
            CodeValidator.ValidationResult validation = CodeValidator.validate(botTask.getBotCode());
            if (!validation.isValid()) {
                log.error("Bot 代码校验失败 [userId={}]: {}", botTask.getUserId(), validation.getReason());
                setBotMove(0); // 返回默认方向
                return;
            }

            // 准备 bot 代码：添加唯一 ID + 替换 input 文件名
            String code = replaceInputFileName(botTask.getBotCode(), inputFileName);
            code = addUid(code, uid);

            // 编译 bot 代码（编译阶段不受沙箱限制）
            Supplier<Integer> botInterface =
                    Reflect.compile("com.kob.botrunningsystem.utils.Bot" + uid, code).create().get();

            // 写入 input 文件
            try (PrintWriter fileOut = new PrintWriter(inputFile)) {
                fileOut.println(botTask.getInput());
                fileOut.flush();
            }

            // Layer 2: SecurityManager 沙箱执行
            Integer direction;
            BotSecurityManager.enterSandbox(inputFile.getAbsolutePath());
            try {
                direction = botInterface.get();
            } finally {
                BotSecurityManager.exitSandbox();
            }

            log.info("Move -> {} -> {}", botTask.getUserId(), direction);

            // 直接设置游戏下一步（不再通过 HTTP）
            setBotMove(direction);
        } catch (SecurityException e) {
            log.error("Bot 代码被沙箱拦截 [userId={}]: {}", botTask.getUserId(), e.getMessage());
            setBotMove(0);
        } catch (Exception e) {
            log.error("Bot 代码执行异常 [userId={}]: {}", botTask.getUserId(), e.getMessage());
            setBotMove(0);
        } finally {
            // 清理临时文件
            if (inputFile.exists()) {
                inputFile.delete();
            }
        }
    }

    /**
     * 直接将 bot 的移动设置到 GameEngine 中
     */
    private void setBotMove(Integer direction) {
        GameEngine game = botTask.getGame();
        if (game == null) return;

        Integer userId = botTask.getUserId();
        if (game.getPlayerA().getId().equals(userId)) {
            game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(userId)) {
            game.setNextStepB(direction);
        }
    }
}
