package utils;

import apptags.Main;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import model.ColorTag;
import model.Tag;

public class PAPIHook extends PlaceholderExpansion {

    private final Main plugin;

    public PAPIHook(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull
    String getIdentifier() {
        // TODO Auto-generated method stub
        return "mtags";
    }

    @Override
    public @NotNull
    String getAuthor() {
        // TODO Auto-generated method stub
        return "Giyu";
    }

    @Override
    public @NotNull
    String getVersion() {
        // TODO Auto-generated method stub
        return "1.0";
    }

    @Override
    public @Nullable
    String onPlaceholderRequest(Player player, String params) {
        if (params.equalsIgnoreCase("player_tag")) {
            if (plugin.getTagsManager().isInRamTagsMap(player)) {
                String[] data = plugin.getTagsManager().getArrayDataPlayer(player);
                String tagName = data[0];
                if (!tagName.equalsIgnoreCase("none")) {
                    return plugin.getTagsManager().getRamTagPlayer(player).getSymbol();
                } else {
                    return "NONE";
                }
            }
        }
        if (params.equalsIgnoreCase("player_color_tag")) {
            if (plugin.getTagsManager().isInRamTagsMap(player)) {
                return plugin.getTagsManager().getRamColorTagPlayer(player).toString();
            }
        }
        if (params.equalsIgnoreCase("format_player_tag")) {
            if (plugin.getTagsManager().isInRamTagsMap(player)) {
                String[] data = plugin.getTagsManager().getArrayDataPlayer(player);
                String tagName = data[0];
                if (!tagName.equalsIgnoreCase("none")) {
                    Tag tag = plugin.getTagsManager().getRamTagPlayer(player);
                    ColorTag.ColorTagUtils colorUtils = new ColorTag.ColorTagUtils(plugin);
                    return colorUtils.applyingColorTag(player, tag);
                } else {
                    return "NONE";
                }
            }
        }
        return null;
    }
}
