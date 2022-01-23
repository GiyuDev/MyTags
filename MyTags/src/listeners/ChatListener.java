package listeners;

import apptags.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.clip.placeholderapi.PlaceholderAPI;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (Main.configuration.getBoolean("other.applying_tag_in_chat")) {
            String format = Main.configuration.getString("other.formated_chat");
            Player p = e.getPlayer();
            if (Main.checkPapiHook()) {
                format = PlaceholderAPI.setPlaceholders(p, format);
            }
            format = format.replace("%message%", e.getMessage());
            format = format.replace("%player%", p.getDisplayName());
            format = ChatColor.translateAlternateColorCodes('&', format);
            e.setFormat(format);
        }
    }
}
