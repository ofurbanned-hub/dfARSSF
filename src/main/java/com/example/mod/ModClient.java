package com.example.mod;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public final class ModClient implements PreLaunchEntrypoint {
    @Override public void onPreLaunch() {
        if (!System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win")) return;
        Path config = Paths.get("config", "mod.properties");
        Properties p = new Properties();
        p.setProperty("enabled", "false"); // Opt in before the prank can launch external applications.
        p.setProperty("message", "get ratted by e4 ez");
        p.setProperty("startup_delay_ms", "1000");
        p.setProperty("min_delay_ms", "80");
        p.setProperty("max_delay_ms", "180");
        try {
            Files.createDirectories(config.getParent());
            if (Files.exists(config)) {
                try (InputStream in = Files.newInputStream(config)) { p.load(in); }
            } else {
                try (OutputStream out = Files.newOutputStream(config)) {
                    p.store(out, "Optional Notepad prank: set enabled=true to activate. No Windows shutdown/restart.");
                }
            }
        } catch (IOException e) { System.err.println("Mod configuration error: " + e.getMessage()); return; }
        if (!Boolean.parseBoolean(p.getProperty("enabled", "false"))) return;
        String message = p.getProperty("message", "get ratted by e4 ez");
        int startup = bounded(p.getProperty("startup_delay_ms"), 1000, 0, 10000);
        int min = bounded(p.getProperty("min_delay_ms"), 80, 20, 2000);
        int max = bounded(p.getProperty("max_delay_ms"), 180, min, 3000);
        new Thread(() -> runPrank(message, startup, min, max), "mod-notepad-prank").start();
    }
    private static int bounded(String raw, int fallback, int low, int high) {
        try { int n = Integer.parseInt(raw); return Math.max(low, Math.min(high, n)); }
        catch (Exception ignored) { return fallback; }
    }
    private static void runPrank(String message, int startup, int min, int max) {
        try {
            Thread.sleep(startup);
            new ProcessBuilder("notepad.exe").start();
            Thread.sleep(1200);
            Robot robot = new Robot();
            for (char c : message.toCharArray()) {
                if (c == '\n') { tap(robot, KeyEvent.VK_ENTER, false); }
                else if (c == ' ') { tap(robot, KeyEvent.VK_SPACE, false); }
                else {
                    int key = KeyEvent.getExtendedKeyCodeForChar(c);
                    if (key == KeyEvent.VK_UNDEFINED) continue;
                    boolean shift = Character.isUpperCase(c) || "!@#$%^&*()_+{}|:\"<>?".indexOf(c) >= 0;
                    tap(robot, key, shift);
                }
                Thread.sleep(ThreadLocalRandom.current().nextInt(min, max + 1));
            }
        } catch (Exception e) { System.err.println("Mod Notepad prank failed: " + e.getMessage()); }
    }
    private static void tap(Robot robot, int key, boolean shift) {
        if (shift) robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(key); robot.keyRelease(key);
        if (shift) robot.keyRelease(KeyEvent.VK_SHIFT);
    }
}
