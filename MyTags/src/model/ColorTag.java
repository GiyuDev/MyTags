package model;

import apptags.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum ColorTag {

    NONE("none"),
    RED("red"),
    YELLOW("yellow"),
    BLUE("blue"),
    GREEN("green"),
    GRAY("gray"),
    BLACK("black"),
    PURPLE("purple"),
    PINK("pink");

    private String id;

    ColorTag(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static class ColorTagUtils {

        private final Main plugin;

        public ColorTagUtils(Main plugin) {
            this.plugin = plugin;
        }

        public String applyingColorTag(Player p, Tag tag) {
            if (plugin.getTagsManager().isInRamTagsMap(p)) {
                String toReturn = "";
                if (plugin.getTagsManager().getRamColorTagPlayer(p).equalsIgnoreCase(ColorTag.RED.getId())) {
                    toReturn = ChatColor.RED + tag.getSymbol();
                } else if (plugin.getTagsManager().getRamColorTagPlayer(p).equalsIgnoreCase(ColorTag.YELLOW.getId())) {
                    toReturn = ChatColor.YELLOW + tag.getSymbol();
                } else if (plugin.getTagsManager().getRamColorTagPlayer(p).equalsIgnoreCase(ColorTag.BLUE.getId())) {
                    toReturn = ChatColor.BLUE + tag.getSymbol();
                } else if (plugin.getTagsManager().getRamColorTagPlayer(p).equalsIgnoreCase(ColorTag.GREEN.getId())) {
                    toReturn = ChatColor.GREEN + tag.getSymbol();
                } else if (plugin.getTagsManager().getRamColorTagPlayer(p).equalsIgnoreCase(ColorTag.PINK.getId())) {
                    toReturn = ChatColor.LIGHT_PURPLE + tag.getSymbol();
                } else if (plugin.getTagsManager().getRamColorTagPlayer(p).equalsIgnoreCase(ColorTag.PURPLE.getId())) {
                    toReturn = ChatColor.DARK_PURPLE + tag.getSymbol();
                } else if (plugin.getTagsManager().getRamColorTagPlayer(p).equalsIgnoreCase(ColorTag.BLACK.getId())) {
                    toReturn = ChatColor.BLACK + tag.getSymbol();
                } else if (plugin.getTagsManager().getRamColorTagPlayer(p).equalsIgnoreCase(ColorTag.GRAY.getId())) {
                    toReturn = ChatColor.GRAY + tag.getSymbol();
                } else if (plugin.getTagsManager().getRamColorTagPlayer(p).equalsIgnoreCase(ColorTag.NONE.getId())) {
                    toReturn = tag.getSymbol();
                } else {
                    toReturn = tag.getSymbol();
                }
                return toReturn;
            }
            return "";
        }
    }
}
