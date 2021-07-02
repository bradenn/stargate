package com.bradenn.stargates.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class HelpCommand implements SubCommand {

    public String getLabel() {
        return "help";
    }

    public String getDescription() {
        return "Get help with the stargate plugin.";
    }

    public void run(Player player, String[] args) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        assert bookMeta != null;
        bookMeta.setTitle("Stargate: A Guide");
        bookMeta.setAuthor("Stargate");
        bookMeta.addPage("§8§lStargate\n§8§oThe definitive guide", "Suck a duck.");
        book.setItemMeta(bookMeta);
        player.openBook(book);

        player.sendMessage("Here is your help: Suck a duck.");
    }

}
