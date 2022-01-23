package apptags;

import commands.AdminCMD;
import commands.TagsCMD;
import database.MySQLDatabase;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import listeners.ChatListener;
import listeners.PlayerListener;
import manager.TagsManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.clip.placeholderapi.PlaceholderAPI;
import menu.ColorMenu;
import menu.TagMenu;
import mysql.MySQL;
import utils.PAPIHook;
import utils.XSound;

public class Main extends JavaPlugin {

    public String rutaConfig;
    public static File configyml;
    public static YamlConfiguration configuration;

    public String rutaConfig2;
    public static File tagsyml;
    public static YamlConfiguration tagsYaml;

    public String rutaConfig3;
    public static File menusyml;
    public static YamlConfiguration menusYaml;

    private MySQL mySQL;

    public MySQL getMySQL() {
        return mySQL;
    }

    private MySQLDatabase mySQLDatabase;

    public MySQLDatabase getMySQLDatabase() {
        return mySQLDatabase;
    }

    private static boolean isPapiHook = false;

    public static boolean checkPapiHook() {
        return isPapiHook;
    }

    private PAPIHook papiHook;

    private TagsManager tagsManager;

    public TagsManager getTagsManager() {
        return tagsManager;
    }

    private TagMenu tagMenu;

    public TagMenu getTagMenu() {
        return tagMenu;
    }

    private ColorMenu colorMenu;

    public ColorMenu getColorMenu() {
        return colorMenu;
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Loading...");
        loadYamlFiles();
        registerCmds();
        this.mySQL = new MySQL();
        this.mySQL.setHost(configuration.getString("database.MySQL_credential.host"));
        this.mySQL.setDatabase(configuration.getString("database.MySQL_credential.database"));
        this.mySQL.setUser(configuration.getString("database.MySQL_credential.username"));
        this.mySQL.setPassword(configuration.getString("database.MySQL_credential.password"));
        this.mySQL.setPort(configuration.getInt("database.MySQL_credential.port"));
        this.mySQL.setSSL(configuration.getString("database.MySQL_credential.useSSL"));
        this.mySQL.config();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            try {
                synchronized (this.mySQL.setConnection()) {
                    try (PreparedStatement stmt = this.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tagsData(player varchar(32), tagName varchar(16), colorTag varchar(16))")) {
                        stmt.executeUpdate();
                    }
                    mySQLDatabase = new MySQLDatabase(mySQL);
                    this.getLogger().info("Correctly connected to your MySQL database");
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                checkHooks();
                tagsManager = new TagsManager(this);
                tagMenu = new TagMenu(this);
                colorMenu = new ColorMenu(this);
                registerListeners(new PlayerListener(this), tagMenu, new ChatListener(), colorMenu);
            }
        }, 1L);
    }

    public void registerConfiguration() {
        rutaConfig = configyml.getAbsolutePath();
        if (!(configyml.exists())) {
            setConfigurationDefaults();
        }
    }

    public void setConfigurationDefaults() {
        this.saveResource("config.yml", true);
    }

    public void registerConfiguration2() {
        rutaConfig2 = tagsyml.getAbsolutePath();
        if (!(tagsyml.exists())) {
            setConfigurationDefaults2();
        }
    }

    public void setConfigurationDefaults2() {
        this.saveResource("tags.yml", true);
    }

    public void registerConfiguration3() {
        rutaConfig3 = menusyml.getAbsolutePath();
        if (!(menusyml.exists())) {
            setConfigurationDefaults3();
        }
    }

    public void setConfigurationDefaults3() {
        this.saveResource("menus.yml", true);
    }

    private void loadYamlFiles() {
        configyml = new File(this.getDataFolder(), "config.yml");
        registerConfiguration();
        configuration = YamlConfiguration.loadConfiguration(configyml);
        tagsyml = new File(this.getDataFolder(), "tags.yml");
        registerConfiguration2();
        tagsYaml = YamlConfiguration.loadConfiguration(tagsyml);
        menusyml = new File(this.getDataFolder(), "menus.yml");
        registerConfiguration3();
        menusYaml = YamlConfiguration.loadConfiguration(menusyml);
    }

    private void checkHooks() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            isPapiHook = true;
            papiHook = new PAPIHook(this);
            papiHook.register();
            this.getLogger().info("PlaceholderAPI was correctly hooked to MyTags");
        } else {
            this.getLogger().warning("PlaceholderAPI wasn't hooked to MyTags due to it's disabled");
        }
    }

    public void msgPlayer(Player p, @Nullable Player target, String msg) {
        if (p != null && p.isOnline()) {
            if (checkPapiHook()) {
                msg = PlaceholderAPI.setPlaceholders(p, msg);
            }
            if (msg.contains("%target_player%")) {
                if (target.isOnline() && target != null) {
                    msg = msg.replace("%target_player%", target.getName());
                } else {
                    msg = msg.replace("%target_player%", "null");
                }
            }
            msg = msg.replace("%prefix%", configuration.getString("prefix"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        } else {
            this.getLogger().severe("Can't send a message to a null player");
        }
    }

    public List<String> colorList(List<String> list, @NotNull Player player) {
        if (checkPapiHook()) {
            return list.stream()
                    .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                    .map(p -> p = PlaceholderAPI.setPlaceholders(player, p)).collect(Collectors.toList());
        } else {
            return list.stream()
                    .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        }
    }

    private void registerCmds() {
        this.getCommand("mytags").setExecutor(new AdminCMD(this));
        this.getCommand("tags").setExecutor(new TagsCMD(this));
    }

    private void registerListeners(Listener... listener) {
        Arrays.stream(listener).forEach(l -> {
            Bukkit.getPluginManager().registerEvents(l, this);
        });
    }

    public void playSoundPlayer(Player p, String path) {
        String[] split = path.split(":");
        XSound.play(p, split[0], Float.valueOf(split[1]), Float.valueOf(split[2]));
    }
}
