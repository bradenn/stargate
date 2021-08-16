package com.bradenn.stargates.cosmetics;

import com.bradenn.stargates.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class Messages {

    public static final ChatColor
            info = ChatColor.of("#0a84ff"),
            compliment = ChatColor.of("#ff9f0a"),
            secondary = ChatColor.of("#64d2ff"),
            danger = ChatColor.of("#ff453a"),
            mono0 = ChatColor.of("#7C7C80"),
            mono1 = ChatColor.of("#8e8e93"),
            mono2 = ChatColor.of("#aeaeb2"),
            mono3 = ChatColor.of("#c7c7cc"),
            mono4 = ChatColor.of("#D1D1D6");

    private static final String prefix = String.format("%s%s %s→", compliment, Main.getPluginName(), mono2);

    private static String format(boolean error, String message, Object... args) {
        String formatted = String.format(message, args);
        String colored = ChatColor.translateAlternateColorCodes('&', formatted);
        return String.format("%s %s%s", prefix, error ? danger : mono3, colored);
    }

    public static void sendInfo(Player player, String message, Object... args) {
        player.sendMessage(format(false, message, args));
    }

    public static void sendRaw(Player player, String message, Object... args) {
        player.sendMessage(StringUtils.format(message, args));
    }

    public static void sendError(Player player, String message, Object... args) {
        player.sendMessage(format(true, message, args));
    }

    public static void sendAction(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

}
