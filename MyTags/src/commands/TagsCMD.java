package commands;

import apptags.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagsCMD implements CommandExecutor {

    private final Main plugin;

    public TagsCMD(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.getLogger().warning("You can't execute this command in console!");
            return false;
        } else {
            Player p = (Player) sender;
            if (args.length < 1) {
                if (p.hasPermission("mytags.open") || p.hasPermission("mytags.*")) {
                    plugin.getTagMenu().open(p);
                    if (Main.configuration.getBoolean("other.sound_on_open_menus.use")) {
                        plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_open_menus.sound"));
                    }
                } else {
                    plugin.msgPlayer(p, null, Main.configuration.getString("no_permission"));
                }
            }
        }
        return true;
    }
}
