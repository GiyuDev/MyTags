package menu;

import apptags.Main;
import java.sql.SQLException;
import java.util.List;
import model.Tag;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class TagMenu implements Listener {

    private Inventory inv;
    private final Main plugin;

    public TagMenu(Main plugin) {
        this.plugin = plugin;
        this.inv = Bukkit.createInventory(null, Main.menusYaml.getInt("menu_tags_config.size"), ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_tags_config.title")));
    }

    private Inventory getInv() {
        return this.inv;
    }

    @SuppressWarnings("deprecation")
    public void open(Player p) {
        if (this.getInv() != null) {
            if (plugin.getTagsManager().isInRamTagsMap(p)) {
                for (String values : Main.tagsYaml.getConfigurationSection("tags").getKeys(false)) {
                    ItemStack item = new ItemStack(Material.valueOf(Main.menusYaml.getString("menu_tags_config.tags_item_material").toUpperCase()), 1, (short) Main.menusYaml.getInt("menu_tags_config.data"));
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.tagsYaml.getString("tags." + values + ".in_menu.name")));
                    List<String> lore_list = Main.tagsYaml.getStringList("tags." + values + ".in_menu.lore");
                    List<String> translated_lore = plugin.colorList(lore_list, p);
                    meta.setLore(translated_lore);
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    item.setItemMeta(meta);
                    this.getInv().setItem(Main.tagsYaml.getInt("tags." + values + ".in_menu.slot"), item);
                }
                if (Main.menusYaml.getBoolean("menu_tags_config.items.decorative_item.use")) {
                    ItemStack decorative = new ItemStack(Material.valueOf(Main.menusYaml.getString("menu_tags_config.items.decorative_item.material").toUpperCase()), 1, (short) Main.menusYaml.getInt("menu_tags_config.items.decorative_item.data"));
                    ItemMeta decorative_meta = decorative.getItemMeta();
                    decorative_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_tags_config.items.decorative_item.name")));
                    List<String> lore_d_list = Main.menusYaml.getStringList("menu_tags_config.items.decorative_item.lore");
                    List<String> translated_d_lore = plugin.colorList(lore_d_list, p);
                    decorative_meta.setLore(translated_d_lore);
                    decorative_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    decorative.setItemMeta(decorative_meta);
                    if (!Main.menusYaml.getBoolean("menu_tags_config.items.decorative_item.use_all_emtpy_slots")) {
                        Main.menusYaml.getIntegerList("menu_tags_config.items.decorative_item.slot_list").forEach(slot -> {
                            getInv().setItem(slot, decorative);
                        });
                    } else {
                        for (int i = 0; i < getInv().getSize(); i++) {
                            if (getInv().getItem(i) == null) {
                                getInv().setItem(i, decorative);
                            } else if (getInv().getItem(i).getType().equals(Material.AIR)) {
                                getInv().setItem(i, decorative);
                            }
                        }
                    }
                }
                ItemStack close = new ItemStack(Material.valueOf(Main.menusYaml.getString("menu_tags_config.items.close_item.material").toUpperCase()), 1, (short) Main.menusYaml.getInt("menu_tags_config.items.close_item.data"));
                ItemMeta close_meta = close.getItemMeta();
                close_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_tags_config.items.close_item.name")));
                List<String> lore_c_list = Main.menusYaml.getStringList("menu_tags_config.items.close_item.lore");
                List<String> translated_lore_c = plugin.colorList(lore_c_list, p);
                close_meta.setLore(translated_lore_c);
                close_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                close.setItemMeta(close_meta);
                this.getInv().setItem(Main.menusYaml.getInt("menu_tags_config.items.close_item.slot"), close);
                if (Main.menusYaml.getBoolean("menu_tags_config.items.preview_item.use")) {
                    ItemStack preview = null;
                    String version = Bukkit.getBukkitVersion();
                    if (!version.contains("1.8")) {
                        preview = new ItemStack(Material.PLAYER_HEAD);
                    } else {
                        preview = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
                    }
                    SkullMeta skull_meta = (SkullMeta) preview.getItemMeta();
                    skull_meta.setOwner(p.getName());
                    skull_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_tags_config.items.preview_item.name")));
                    List<String> skull_lore = Main.menusYaml.getStringList("menu_tags_config.items.preview_item.lore");
                    List<String> translated_skull_lore = plugin.colorList(skull_lore, p);
                    skull_meta.setLore(translated_skull_lore);
                    skull_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    preview.setItemMeta(skull_meta);
                    this.getInv().setItem(Main.menusYaml.getInt("menu_tags_config.items.preview_item.slot"), preview);
                }
                ItemStack color_item = new ItemStack(Material.valueOf(Main.menusYaml.getString("menu_tags_config.items.colorTag_menu_item.material").toUpperCase()), 1, (short) Main.menusYaml.getInt("menu_tags_config.items.colorTag_menu_item.data"));
                ItemMeta color_meta = color_item.getItemMeta();
                color_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_tags_config.items.colorTag_menu_item.name")));
                List<String> color_lore = Main.menusYaml.getStringList("menu_tags_config.items.colorTag_menu_item.lore");
                List<String> replace_color_lore = plugin.colorList(color_lore, p);
                color_meta.setLore(replace_color_lore);
                color_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                color_item.setItemMeta(color_meta);
                this.getInv().setItem(Main.menusYaml.getInt("menu_tags_config.items.colorTag_menu_item.slot"), color_item);
                p.openInventory(getInv());
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_tags_config.title")))) {
            Player p = (Player) e.getPlayer();
            if (Main.configuration.getBoolean("other.sound_on_close_menus.use")) {
                plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_close_menus.sound"));
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_tags_config.title")))) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item == null || e.getInventory().getItem(e.getSlot()).getType().equals(Material.AIR) || e.getInventory().getItem(e.getSlot()) == null) {
                return;
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_tags_config.items.close_item.name")))) {
                p.closeInventory();
            }
            for (Tag tag : plugin.getTagsManager().getTagList()) {
                try {
                    if (plugin.getTagsManager().isInRamTagsMap(p) && plugin.getMySQLDatabase().playerExists(p)) {
                        if (item.getItemMeta().getDisplayName().contains(tag.getName())) {
                            if (plugin.getTagsManager().playerHasPermissionNodeTag(p, tag.getName())) {
                                plugin.getTagsManager().updateRamTagPlayer(p, tag.getName());
                                plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.on_tag_update").replace("%tag%", tag.getName()));
                                if (Main.configuration.getBoolean("other.sound_on_tag_update.use")) {
                                    plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_tag_update.sound"));
                                }
                            } else {
                                plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.no_tag_permission"));
                            }
                        }
                    }
                    p.closeInventory();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_tags_config.items.colorTag_menu_item.name")))) {
                if (p.hasPermission("mytags.open") || p.hasPermission("mytags.*")) {
                    p.closeInventory();
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        plugin.getColorMenu().open(p);
                        if (Main.configuration.getBoolean("other.sound_on_open_menus.use")) {
                            plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_open_menus.sound"));
                        }
                    }, 5L);
                }
            }
        }
    }
}
