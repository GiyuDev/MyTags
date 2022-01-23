package listeners;

import apptags.Main;
import java.sql.SQLException;
import model.Tag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final Main plugin;

    public PlayerListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        try {
            plugin.getMySQLDatabase().insertPlayer(p);
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                plugin.getTagsManager().createPlayerOnTagMap(p, plugin.getMySQLDatabase().getPlayerTagName(p), plugin.getMySQLDatabase().getColorTagByIdPlayer(p));
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }, 1L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (plugin.getTagsManager().isInRamTagsMap(p)) {
            Tag current_tag = plugin.getTagsManager().getRamTagPlayer(p);
            String current_color = plugin.getTagsManager().getRamColorTagPlayer(p);
            try {
                plugin.getMySQLDatabase().updateTagPlayer(p, current_tag);
                plugin.getMySQLDatabase().updateColorTagPlayer(p, current_color);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}
