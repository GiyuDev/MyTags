package menu;

import apptags.Main;
import java.sql.SQLException;
import java.util.List;
import model.ColorTag;

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

public class ColorMenu implements Listener {

    private final Main plugin;
    private Inventory inv;

    public ColorMenu(Main plugin) {
        this.plugin = plugin;
        this.inv = Bukkit.createInventory(null, Main.menusYaml.getInt("menu_color_tags.size"), ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_color_tags.title")));
    }

    private Inventory getInv() {
        return this.inv;
    }

    @SuppressWarnings("deprecation")
    public void open(Player p) {
        if (this.getInv() != null) {
            if (plugin.getTagsManager().isInRamTagsMap(p)) {
                for (String values : Main.menusYaml.getConfigurationSection("colors_in_menu").getKeys(false)) {
                    ItemStack item = new ItemStack(Material.valueOf(Main.menusYaml.getString("colors_in_menu." + values + ".material").toUpperCase()), 1, (short) Main.menusYaml.getInt("colors_in_menu." + values + ".data"));
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("colors_in_menu." + values + ".name")));
                    List<String> lore = Main.menusYaml.getStringList("colors_in_menu." + values + ".lore");
                    List<String> translated_l = plugin.colorList(lore, p);
                    meta.setLore(translated_l);
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    item.setItemMeta(meta);
                    this.getInv().setItem(Main.menusYaml.getInt("colors_in_menu." + values + ".slot"), item);
                }
                if (Main.menusYaml.getBoolean("menu_color_tags.items.decorative_item.use")) {
                    ItemStack decorative = new ItemStack(Material.valueOf(Main.menusYaml.getString("menu_color_tags.items.decorative_item.material").toUpperCase()), 1, (short) Main.menusYaml.getInt("menu_color_tags.items.decorative_item.data"));
                    ItemMeta meta_dec = decorative.getItemMeta();
                    meta_dec.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_color_tags.items.decorative_item.name")));
                    List<String> lore_dec = Main.menusYaml.getStringList("menu_color_tags.items.decorative_item.lore");
                    List<String> translated_lore_d = plugin.colorList(lore_dec, p);
                    meta_dec.setLore(translated_lore_d);
                    meta_dec.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    decorative.setItemMeta(meta_dec);
                    if (!Main.menusYaml.getBoolean("menu_color_tags.items.decorative_item.use_all_emtpy_slots")) {
                        Main.menusYaml.getIntegerList("menu_color_tags.items.decorative_item.slot_list").forEach(slot -> {
                            this.getInv().setItem(slot, decorative);
                        });
                    } else {
                        for (int i = 0; i < this.getInv().getSize(); i++) {
                            if (this.getInv().getItem(i) == null) {
                                this.getInv().setItem(i, decorative);
                            } else if (this.getInv().getItem(i).getType().equals(Material.AIR)) {
                                this.getInv().setItem(i, decorative);
                            }
                        }
                    }
                }
                ItemStack close = new ItemStack(Material.valueOf(Main.menusYaml.getString("menu_color_tags.items.close_item.material").toUpperCase()), 1, (short) Main.menusYaml.getInt("menu_color_tags.items.close_item.data"));
                ItemMeta meta_close = close.getItemMeta();
                meta_close.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_color_tags.items.close_item.name")));
                List<String> lore_close = Main.menusYaml.getStringList("menu_color_tags.items.close_item.lore");
                List<String> translated_lore_close = plugin.colorList(lore_close, p);
                meta_close.setLore(translated_lore_close);
                meta_close.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                close.setItemMeta(meta_close);
                this.getInv().setItem(Main.menusYaml.getInt("menu_color_tags.items.close_item.slot"), close);
                ItemStack tag_menu = new ItemStack(Material.valueOf(Main.menusYaml.getString("menu_color_tags.items.tag_menu_item.material").toUpperCase()), 1, (short) Main.menusYaml.getInt("menu_color_tags.items.tag_menu_item.data"));
                ItemMeta meta_tag = tag_menu.getItemMeta();
                meta_tag.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_color_tags.items.tag_menu_item.name")));
                List<String> lore_tag = Main.menusYaml.getStringList("menu_color_tags.items.tag_menu_item.lore");
                List<String> translated_lore_tag = plugin.colorList(lore_tag, p);
                meta_tag.setLore(translated_lore_tag);
                meta_tag.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                tag_menu.setItemMeta(meta_tag);
                this.getInv().setItem(Main.menusYaml.getInt("menu_color_tags.items.tag_menu_item.slot"), tag_menu);
                p.openInventory(this.getInv());
            }
        }
    }

    @EventHandler
    public void onCloseColor(InventoryCloseEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_color_tags.title")))) {
            Player p = (Player) e.getPlayer();
            if (Main.configuration.getBoolean("other.sound_on_close_menus.use")) {
                plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_close_menus.sound"));
            }
        }
    }

    @EventHandler
    public void onClickColor(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_color_tags.title")))) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item == null || e.getInventory().getItem(e.getSlot()).getType().equals(Material.AIR) || e.getInventory().getItem(e.getSlot()) == null) {
                return;
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_color_tags.items.close_item.name")))) {
                p.closeInventory();
            }
            try {
                if (plugin.getTagsManager().isInRamTagsMap(p) && plugin.getMySQLDatabase().playerExists(p)) {
                    if (plugin.getTagsManager().getRamTagPlayer(p) != null) {
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("colors_in_menu.red_item.name")))) {
                            plugin.getTagsManager().updateRamColorPlayer(p, ColorTag.RED.getId());
                            plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.on_color_tag_update").replace("%color%", ColorTag.RED.toString()));
                            if (Main.configuration.getBoolean("other.sound_on_color_tag_update.use")) {
                                plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_color_tag_update.sound"));
                            }
                        }
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("colors_in_menu.yellow_item.name")))) {
                            plugin.getTagsManager().updateRamColorPlayer(p, ColorTag.YELLOW.getId());
                            plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.on_color_tag_update").replace("%color%", ColorTag.YELLOW.toString()));
                            if (Main.configuration.getBoolean("other.sound_on_color_tag_update.use")) {
                                plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_color_tag_update.sound"));
                            }
                        }
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("colors_in_menu.blue_item.name")))) {
                            plugin.getTagsManager().updateRamColorPlayer(p, ColorTag.BLUE.getId());
                            plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.on_color_tag_update").replace("%color%", ColorTag.BLUE.toString()));
                            if (Main.configuration.getBoolean("other.sound_on_color_tag_update.use")) {
                                plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_color_tag_update.sound"));
                            }
                        }
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("colors_in_menu.green_item.name")))) {
                            plugin.getTagsManager().updateRamColorPlayer(p, ColorTag.GREEN.getId());
                            plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.on_color_tag_update").replace("%color%", ColorTag.GREEN.toString()));
                            if (Main.configuration.getBoolean("other.sound_on_color_tag_update.use")) {
                                plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_color_tag_update.sound"));
                            }
                        }
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("colors_in_menu.gray_item.name")))) {
                            plugin.getTagsManager().updateRamColorPlayer(p, ColorTag.GRAY.getId());
                            plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.on_color_tag_update").replace("%color%", ColorTag.GRAY.toString()));
                            if (Main.configuration.getBoolean("other.sound_on_color_tag_update.use")) {
                                plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_color_tag_update.sound"));
                            }
                        }
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("colors_in_menu.black_item.name")))) {
                            plugin.getTagsManager().updateRamColorPlayer(p, ColorTag.BLACK.getId());
                            plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.on_color_tag_update").replace("%color%", ColorTag.BLACK.toString()));
                            if (Main.configuration.getBoolean("other.sound_on_color_tag_update.use")) {
                                plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_color_tag_update.sound"));
                            }
                        }
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("colors_in_menu.pink_item.name")))) {
                            plugin.getTagsManager().updateRamColorPlayer(p, ColorTag.PINK.getId());
                            plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.on_color_tag_update").replace("%color%", ColorTag.PINK.toString()));
                            if (Main.configuration.getBoolean("other.sound_on_color_tag_update.use")) {
                                plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_color_tag_update.sound"));
                            }
                        }
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("colors_in_menu.purple_item.name")))) {
                            plugin.getTagsManager().updateRamColorPlayer(p, ColorTag.PURPLE.getId());
                            plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.on_color_tag_update").replace("%color%", ColorTag.PURPLE.toString()));
                            if (Main.configuration.getBoolean("other.sound_on_color_tag_update.use")) {
                                plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_color_tag_update.sound"));
                            }
                        }
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("colors_in_menu.none_item.name")))) {
                            plugin.getTagsManager().updateRamColorPlayer(p, ColorTag.NONE.getId());
                            plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.on_color_tag_update").replace("%color%", ColorTag.NONE.toString()));
                            if (Main.configuration.getBoolean("other.sound_on_color_tag_update.use")) {
                                plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_color_tag_update.sound"));
                            }
                        }
                        p.closeInventory();
                    } else {
                        plugin.msgPlayer(p, null, Main.configuration.getString("other.messages.null_tag"));
                        p.closeInventory();
                    }
                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Main.menusYaml.getString("menu_color_tags.items.tag_menu_item.name")))) {
                p.closeInventory();
                Bukkit.getScheduler().runTaskLater(plugin, ()->{
                    plugin.getTagMenu().open(p);
                    if (Main.configuration.getBoolean("other.sound_on_open_menus.use")) {
                            plugin.playSoundPlayer(p, Main.configuration.getString("other.sound_on_open_menus.sound"));
                        }
                }, 5L);
            }
        }
    }
}
