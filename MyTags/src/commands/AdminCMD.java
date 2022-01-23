package commands;

import apptags.Main;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.Tag;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class AdminCMD implements CommandExecutor {

    private Main plugin;

    public AdminCMD(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.getLogger().warning("Command can't be executed in console!");
            return false;
        } else {
            Player p = (Player) sender;
            if (args.length < 1) {
                if (p.hasPermission("mytags.admin") || p.hasPermission("mytags.*")) {
                    plugin.msgPlayer(p, null, "%prefix% &fPlugin made by: &6Giyu");
                } else {
                    plugin.msgPlayer(p, null, Main.configuration.getString("no_permission"));
                }
            } else {
                if (p.hasPermission("mytags.admin") || p.hasPermission("mytags.*")) {
                    switch (args[0]) {
                        case "reload":
                            Main.configyml = new File(plugin.getDataFolder(), "config.yml");
                            Main.configuration = YamlConfiguration.loadConfiguration(Main.configyml);
                            Main.tagsyml = new File(plugin.getDataFolder(), "tags.yml");
                            Main.tagsYaml = YamlConfiguration.loadConfiguration(Main.tagsyml);
                            Main.menusyml = new File(plugin.getDataFolder(), "menus.yml");
                            Main.menusYaml = YamlConfiguration.loadConfiguration(Main.menusyml);
                            plugin.msgPlayer(p, null, "%prefix% &fPlugin files correctly reloaded!");
                            break;
                        case "taglist":
                            for (int i = 0; i < plugin.getTagsManager().getTagList().size(); i++) {
                                Tag tag = plugin.getTagsManager().getTagList().get(i);
                                ArrayList<String> msgList = new ArrayList<>();
                                msgList.add("&8<==========================================>");
                                msgList.add("&f");
                                msgList.add("&aTag: &e" + tag.getName());
                                msgList.add("&aSymbol: &e" + tag.getSymbol());
                                msgList.add("&f");
                                if (i == plugin.getTagsManager().getTagList().size() - 1) {
                                    msgList.add("&8<==========================================>");
                                }
                                List<String> converted_msg = plugin.colorList(msgList, p);
                                converted_msg.forEach(m -> {
                                    plugin.msgPlayer(p, null, m);
                                });
                            }
                            break;
                    }
                } else {
                    plugin.msgPlayer(p, null, Main.configuration.getString("no_permission"));
                }
            }
        }
        return true;
    }
}
