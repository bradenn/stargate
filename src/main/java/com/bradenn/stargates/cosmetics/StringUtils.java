package com.bradenn.stargates.cosmetics;

import net.md_5.bungee.api.ChatColor;

public class StringUtils {

    public static String format(String message, Object... args) {
        String formatted = String.format(message, args);
        return ChatColor.translateAlternateColorCodes('&', formatted);
    }

}
