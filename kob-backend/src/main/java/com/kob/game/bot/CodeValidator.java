package com.kob.game.bot;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Bot 代码静态安全检查器
 * 在编译前扫描用户代码，拒绝包含危险 API 调用的代码
 */
public class CodeValidator {

    /** 允许的 import 白名单 */
    private static final List<String> ALLOWED_IMPORTS = Arrays.asList(
        "java.util.",
        "java.io.File",
        "java.io.FileNotFoundException",
        "java.io.Scanner",
        "java.util.function.Supplier"
    );

    /**
     * 黑名单正则 — 使用正则精确匹配，避免 RuntimeException 等合法类名被误杀
     * 每个 Pattern 匹配一种危险用法
     */
    private static final List<Pattern> BLACKLIST_PATTERNS = Arrays.asList(
        // Runtime.getRuntime() / Runtime.class 等，但不匹配 RuntimeException
        Pattern.compile("\\bRuntime\\s*\\."),
        Pattern.compile("\\bRuntime\\s*\\.\\s*getRuntime"),
        Pattern.compile("\\bProcessBuilder\\b"),
        Pattern.compile("\\bSystem\\s*\\.\\s*exit\\b"),
        Pattern.compile("\\bSystem\\s*\\.\\s*setSecurityManager\\b"),
        // ClassLoader 但不匹配注释中的 ClassLoader
        Pattern.compile("\\bClassLoader\\b"),
        Pattern.compile("\\bURLClassLoader\\b"),
        // 反射
        Pattern.compile("\\bjava\\.lang\\.reflect\\b"),
        Pattern.compile("\\.forName\\s*\\("),
        Pattern.compile("\\.getMethod\\s*\\("),
        Pattern.compile("\\.getDeclaredMethod\\s*\\("),
        Pattern.compile("\\.getDeclaredField\\s*\\("),
        // 网络
        Pattern.compile("\\bjava\\.net\\b"),
        Pattern.compile("\\bjavax\\.net\\b"),
        Pattern.compile("\\bjava\\.nio\\b"),
        // 进程
        Pattern.compile("\\bProcessHandle\\b"),
        // native/unsafe
        Pattern.compile("\\bnative\\s+\\w"),
        Pattern.compile("\\bUnsafe\\b"),
        // 内部 API
        Pattern.compile("\\bsun\\."),
        Pattern.compile("\\bcom\\.sun\\."),
        // 线程操作（允许使用 Thread 的 sleep 等安全方法不在检查范围，
        // 但禁止 new Thread / extends Thread / ThreadGroup）
        Pattern.compile("new\\s+Thread\\b"),
        Pattern.compile("extends\\s+Thread\\b"),
        Pattern.compile("\\bThreadGroup\\b"),
        // SecurityManager
        Pattern.compile("\\bSecurityManager\\b")
    );

    /** 匹配 import 语句的正则 */
    private static final Pattern IMPORT_PATTERN = Pattern.compile("^\\s*import\\s+([\\w.]+\\*?)\\s*;", Pattern.MULTILINE);

    public static ValidationResult validate(String code) {
        if (code == null || code.trim().isEmpty()) {
            return ValidationResult.fail("Bot 代码为空");
        }

        // 检查 import 语句
        java.util.regex.Matcher matcher = IMPORT_PATTERN.matcher(code);
        while (matcher.find()) {
            String importPath = matcher.group(1);
            boolean allowed = false;
            for (String prefix : ALLOWED_IMPORTS) {
                if (importPath.startsWith(prefix) || importPath.equals(prefix.replace(".", ""))) {
                    allowed = true;
                    break;
                }
            }
            if (!allowed) {
                return ValidationResult.fail("禁止的 import: " + importPath);
            }
        }

        // 去掉字符串字面量和注释后再检查关键词，避免误报
        String codeWithoutStrings = removeStringsAndComments(code);

        for (Pattern pattern : BLACKLIST_PATTERNS) {
            if (pattern.matcher(codeWithoutStrings).find()) {
                return ValidationResult.fail("禁止使用: " + pattern.pattern());
            }
        }

        return ValidationResult.ok();
    }

    /**
     * 移除代码中的字符串字面量和注释，避免黑名单误报
     */
    private static String removeStringsAndComments(String code) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < code.length()) {
            char c = code.charAt(i);
            if (c == '/' && i + 1 < code.length()) {
                if (code.charAt(i + 1) == '/') {
                    // 单行注释，跳到行尾
                    while (i < code.length() && code.charAt(i) != '\n') i++;
                    continue;
                } else if (code.charAt(i + 1) == '*') {
                    // 多行注释
                    i += 2;
                    while (i + 1 < code.length() && !(code.charAt(i) == '*' && code.charAt(i + 1) == '/')) i++;
                    i += 2;
                    continue;
                }
            }
            if (c == '"') {
                // 跳过字符串
                i++;
                while (i < code.length() && code.charAt(i) != '"') {
                    if (code.charAt(i) == '\\') i++; // 跳过转义
                    i++;
                }
                i++;
                continue;
            }
            if (c == '\'') {
                // 跳过字符字面量
                i++;
                while (i < code.length() && code.charAt(i) != '\'') {
                    if (code.charAt(i) == '\\') i++;
                    i++;
                }
                i++;
                continue;
            }
            sb.append(c);
            i++;
        }
        return sb.toString();
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String reason;

        private ValidationResult(boolean valid, String reason) {
            this.valid = valid;
            this.reason = reason;
        }

        public static ValidationResult ok() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult fail(String reason) {
            return new ValidationResult(false, reason);
        }

        public boolean isValid() {
            return valid;
        }

        public String getReason() {
            return reason;
        }
    }
}
