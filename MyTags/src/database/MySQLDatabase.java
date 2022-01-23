package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Tag;
import mysql.MySQL;

import org.bukkit.entity.Player;

public class MySQLDatabase {

    private MySQL mySQL;

    public MySQLDatabase(MySQL mySQL) {
        this.mySQL = mySQL;
    }

    public boolean playerExists(Player p) throws SQLException {
        try (PreparedStatement stmt = mySQL.getConnection().prepareStatement("SELECT * FROM tagsData WHERE player=?")) {
            stmt.setString(1, p.getName());
            ResultSet set = stmt.executeQuery();
            return set.next();
        }
    }

    public void insertPlayer(Player p) throws SQLException {
        if (!playerExists(p)) {
            try (PreparedStatement stmt = mySQL.getConnection().prepareStatement("INSERT INTO tagsData(player, tagName, colorTag) VALUES(?,?,?)")) {
                stmt.setString(1, p.getName());
                stmt.setString(2, "none");
                stmt.setString(3, "none");
                stmt.executeUpdate();
            }
        }
    }

    public void updateTagPlayer(Player p, Tag tag) throws SQLException {
        if (playerExists(p)) {
            try (PreparedStatement stmt = mySQL.getConnection().prepareStatement("UPDATE tagsData SET tagName=? WHERE player=?")) {
                stmt.setString(1, tag.getName());
                stmt.setString(2, p.getName());
                stmt.executeUpdate();
            }
        }
    }

    public void updateColorTagPlayer(Player p, String color) throws SQLException {
        if (playerExists(p)) {
            try (PreparedStatement stmt = mySQL.getConnection().prepareStatement("UPDATE tagsData SET colorTag=? WHERE player=?")) {
                stmt.setString(1, color);
                stmt.setString(2, p.getName());
                stmt.executeUpdate();
            }
        }
    }

    public String getPlayerTagName(Player p) throws SQLException {
        if (playerExists(p)) {
            try (PreparedStatement stmt = mySQL.getConnection().prepareStatement("SELECT tagName FROM tagsData WHERE player=?")) {
                stmt.setString(1, p.getName());
                ResultSet set = stmt.executeQuery();
                if (set.next()) {
                    return set.getString("tagName");
                }
            }
        }
        return null;
    }

    public String getColorTagByIdPlayer(Player p) throws SQLException {
        if (playerExists(p)) {
            try (PreparedStatement stmt = mySQL.getConnection().prepareStatement("SELECT colorTag FROM tagsData WHERE player=?")) {
                stmt.setString(1, p.getName());
                ResultSet set = stmt.executeQuery();
                if (set.next()) {
                    return set.getString("colorTag");
                }
            }
        }
        return null;
    }
}
