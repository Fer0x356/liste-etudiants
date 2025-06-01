package com.dimitri.monapp.utils;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class ConfigUtils {
    private static final String CONFIG_PATH = System.getProperty("user.home") + "/.monapp-config.properties";

    public static void setDarkMode(boolean enabled) {
        Properties props = load();
        props.setProperty("darkMode", Boolean.toString(enabled));
        save(props);
    }

    public static boolean isDarkMode() {
        Properties props = load();
        return Boolean.parseBoolean(props.getProperty("darkMode", "false"));
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(CONFIG_PATH))) {
            props.load(in);
        } catch (IOException ignored) {}
        return props;
    }

    private static void save(Properties props) {
        try (OutputStream out = Files.newOutputStream(Paths.get(CONFIG_PATH))) {
            props.store(out, "MonApp config");
        } catch (IOException ignored) {}
    }
}