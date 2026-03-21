package com.kob.game.bot;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

/**
 * Bot 代码运行时沙箱
 * 通过 ThreadLocal 标记当前线程是否在执行 bot 代码
 * bot 执行时限制文件、网络、进程等权限
 */
public class BotSecurityManager extends SecurityManager {

    private static final ThreadLocal<String> SANDBOX_ACTIVE = new ThreadLocal<>();

    /**
     * 进入沙箱模式，只允许读取指定的 input 文件
     */
    public static void enterSandbox(String allowedInputFile) {
        SANDBOX_ACTIVE.set(allowedInputFile);
    }

    /**
     * 退出沙箱模式
     */
    public static void exitSandbox() {
        SANDBOX_ACTIVE.remove();
    }

    /**
     * 当前线程是否在沙箱中
     */
    private boolean isInSandbox() {
        return SANDBOX_ACTIVE.get() != null;
    }

    private String getAllowedFile() {
        return SANDBOX_ACTIVE.get();
    }

    @Override
    public void checkPermission(Permission perm) {
        if (!isInSandbox()) return;

        String name = perm.getName();
        // 禁止创建 ClassLoader
        if ("createClassLoader".equals(name)) {
            throw new SecurityException("Bot 代码禁止创建 ClassLoader");
        }
        // 禁止设置 SecurityManager
        if ("setSecurityManager".equals(name)) {
            throw new SecurityException("Bot 代码禁止修改 SecurityManager");
        }
        // 禁止访问声明的成员（反射）
        if ("accessDeclaredMembers".equals(name)) {
            throw new SecurityException("Bot 代码禁止使用反射");
        }
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        checkPermission(perm);
    }

    @Override
    public void checkExec(String cmd) {
        if (isInSandbox()) {
            throw new SecurityException("Bot 代码禁止执行系统命令: " + cmd);
        }
    }

    @Override
    public void checkRead(String file) {
        if (isInSandbox()) {
            String allowed = getAllowedFile();
            // 允许读取 input 文件（兼容相对路径和绝对路径）
            if (file.equals(allowed)) return;
            if (new java.io.File(file).getAbsolutePath().equals(allowed)) return;
            if (file.endsWith(".class") || file.endsWith(".jar")) return;
            if (file.startsWith(System.getProperty("java.home"))) return;
            // 允许读取当前工作目录（joor 编译需要）
            if (file.equals(System.getProperty("user.dir"))) return;
            throw new SecurityException("Bot 代码禁止读取文件: " + file);
        }
    }

    @Override
    public void checkRead(String file, Object context) {
        checkRead(file);
    }

    @Override
    public void checkRead(FileDescriptor fd) {
        // 允许标准输入/输出
    }

    @Override
    public void checkWrite(String file) {
        if (isInSandbox()) {
            throw new SecurityException("Bot 代码禁止写入文件: " + file);
        }
    }

    @Override
    public void checkWrite(FileDescriptor fd) {
        if (isInSandbox()) {
            throw new SecurityException("Bot 代码禁止写入文件");
        }
    }

    @Override
    public void checkDelete(String file) {
        if (isInSandbox()) {
            throw new SecurityException("Bot 代码禁止删除文件: " + file);
        }
    }

    @Override
    public void checkConnect(String host, int port) {
        if (isInSandbox()) {
            throw new SecurityException("Bot 代码禁止网络连接: " + host + ":" + port);
        }
    }

    @Override
    public void checkConnect(String host, int port, Object context) {
        checkConnect(host, port);
    }

    @Override
    public void checkListen(int port) {
        if (isInSandbox()) {
            throw new SecurityException("Bot 代码禁止监听端口: " + port);
        }
    }

    @Override
    public void checkAccept(String host, int port) {
        if (isInSandbox()) {
            throw new SecurityException("Bot 代码禁止接受连接: " + host + ":" + port);
        }
    }

    @Override
    public void checkMulticast(InetAddress maddr) {
        if (isInSandbox()) {
            throw new SecurityException("Bot 代码禁止多播");
        }
    }

    @Override
    public void checkExit(int status) {
        if (isInSandbox()) {
            throw new SecurityException("Bot 代码禁止调用 System.exit");
        }
    }

    @Override
    public void checkAccess(Thread t) {
        if (isInSandbox()) {
            throw new SecurityException("Bot 代码禁止操作线程");
        }
    }

    @Override
    public void checkAccess(ThreadGroup g) {
        if (isInSandbox()) {
            throw new SecurityException("Bot 代码禁止操作线程组");
        }
    }
}
