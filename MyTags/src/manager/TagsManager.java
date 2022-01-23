package manager;

import apptags.Main;
import java.util.ArrayList;
import java.util.HashMap;
import model.Tag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class TagsManager {

    private ArrayList<Tag> tagList;
    private HashMap<Player, String[]> tagMap;

    private ArrayList<String> tagListByName;

    private Main plugin;

    public TagsManager(Main plugin) {
        this.plugin = plugin;
        this.tagList = new ArrayList<>(44);
        this.tagMap = new HashMap<>();
        this.tagListByName = new ArrayList<>();
        initTags();
        for (Tag tag : this.getTagList()) {
            plugin.getLogger().info("Tag founded with the name: " + tag.getName() + ", tag correctly loaded!");
            tagListByName.add(tag.getName());
        }
    }

    public ArrayList<Tag> getTagList() {
        return tagList;
    }

    public ArrayList<String> getTagByNameList() {
        return tagListByName;
    }

    public HashMap<Player, String[]> getTagMap() {
        return tagMap;
    }

    public void initTags() {
        int i = 0;
        if (Main.tagsYaml.getConfigurationSection("tags") != null) {
            for (String values : Main.tagsYaml.getConfigurationSection("tags").getKeys(false)) {
                Tag tag = new Tag(
                        Main.tagsYaml.getString("tags." + values + ".name"),
                        Main.tagsYaml.getString("tags." + values + ".symbol"),
                        Main.tagsYaml.getString("tags." + values + ".permissionNode"));
                getTagList().add(tag);
                i += 1;
                if (i == 44) {
                    plugin.getLogger().info("Maximun capacity of tags reached!");
                    break;
                }
            }
            plugin.getLogger().info("All tags correctly loaded!");
        } else {
            plugin.getLogger().info("Tag list is currently empty!");
        }
    }

    public Tag getTagByName(String name) {
        for (Tag tag : this.getTagList()) {
            if (tag.getName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

    public String[] getArrayDataPlayer(Player p) {
        return isInRamTagsMap(p) ? getTagMap().get(p) : new String[3];
    }

    public void createPlayerOnTagMap(Player p, String tagName, String colorID) {
        if (!(this.getTagMap().containsKey(p))) {
            if (tagName.equalsIgnoreCase("none")) {
                String[] data = {"none", colorID};
                this.getTagMap().put(p, data);
            } else {
                String[] data = {tagName, colorID};
                this.getTagMap().put(p, data);
            }

        } else {
            this.getTagMap().remove(p);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                String[] data = {tagName, colorID};
                this.getTagMap().put(p, data);
            }, 1L);
        }
    }

    public void updateRamTagPlayer(Player p, String tagName) {
        if (this.getTagMap().containsKey(p)) {
            String[] data = getArrayDataPlayer(p);
            data[0] = tagName;
            this.getTagMap().put(p, data);
        }
    }

    public void updateRamColorPlayer(Player p, String id) {
        if (this.getTagMap().containsKey(p)) {
            String[] data = getArrayDataPlayer(p);
            data[1] = id;
            this.getTagMap().put(p, data);
        }
    }

    public Tag getRamTagPlayer(Player p) {
        if (this.getTagMap().containsKey(p)) {
            String[] data = getArrayDataPlayer(p);
            String name = data[0];
            if (!name.equalsIgnoreCase("none")) {
                return this.getTagByName(name);
            } else {
                plugin.getLogger().severe("WANRING! GETTING A NULL TAG!");
            }
        }
        return null;
    }

    public String getRamColorTagPlayer(Player p) {
        if (this.getTagMap().containsKey(p)) {
            String[] data = getArrayDataPlayer(p);
            String name = data[1];
            return name;
        }
        return null;
    }

    public boolean isInRamTagsMap(Player p) {
        return getTagMap().containsKey(p);
    }

    public boolean playerHasPermissionNodeTag(Player p, String name) {
        Tag tag = this.getTagByName(name);
        return p.hasPermission(tag.getPermissionNode()) || p.hasPermission("mytags.*") || p.hasPermission("mytags.tags.*");
    }
}
