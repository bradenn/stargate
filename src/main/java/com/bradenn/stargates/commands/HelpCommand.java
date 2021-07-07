package com.bradenn.stargates.commands;

import com.bradenn.stargates.structures.StructureManager;
import com.bradenn.stargates.structures.rings.Rings;
import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand {

    public String getLabel() {
        return "help";
    }

    public String getDescription() {
        return "Get help with the stargate plugin.";
    }

    public void run(Player player, String[] args) throws Exception {
//        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
//        BookMeta bookMeta = (BookMeta) book.getItemMeta();
//        assert bookMeta != null;
//        bookMeta.setTitle("Stargate: A Guide");
//        bookMeta.setAuthor("Stargate");
//        bookMeta.addPage("§8§lStargate\n§8§oThe definitive guide", "Suck a duck.");
//        book.setItemMeta(bookMeta);
//        player.openBook(book);

        Rings stargate = StructureManager.createStructure("anus", player.getLocation(), Rings.class);


        player.sendMessage("Here is your help: Suck a duck.");
    }

}
