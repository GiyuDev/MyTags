package mysql;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;

public class MySQL {

    private String url_jdbc_legacy;
    private String url_updated;

    private HikariDataSource hdsource;
    private Connection connection;

    private String host;
    private String user;
    private String database;
    private Integer port;
    private String password;
    private String SSL;

    public void setHost(String host) {
        this.host = host;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSSL(String SSL) {
        this.SSL = SSL;
    }

    private boolean isFounded(String className) throws ClassNotFoundException {
        Class.forName(className);
        return true;
    }

    public void config() {
        String url = "";
        url_jdbc_legacy = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource";
        url_updated = "com.mysql.cj.jdbc.MysqlDataSource";
        hdsource = new HikariDataSource();
        try {
            if (isFounded(url_jdbc_legacy)) {
                url = url_jdbc_legacy;
            } else if (isFounded(url_updated)) {
                url = url_updated;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        hdsource.setDataSourceClassName(url);
        hdsource.addDataSourceProperty("serverName", host);
        hdsource.addDataSourceProperty("user", user);
        hdsource.addDataSourceProperty("password", password);
        hdsource.addDataSourceProperty("port", port);
        hdsource.addDataSourceProperty("databaseName", database);
        hdsource.addDataSourceProperty("autoReconnect", true);
        hdsource.addDataSourceProperty("characterEncoding", "latin1");
        hdsource.addDataSourceProperty("useSSL", SSL);
        hdsource.setMaximumPoolSize(30);
        hdsource.setMaxLifetime(1000000000);
    }

    public Connection setConnection() throws SQLException {
        connection = hdsource.getConnection();
        return connection;
    }

    public Connection getConnection() throws SQLException {
        return connection.isClosed() && connection == null ? null : connection;
    }

    public boolean isConnected() throws SQLException {
        return !getConnection().isClosed() && getConnection() == null;
    }

    public void disconnect() throws SQLException {
        if (isConnected()) {
            this.getConnection().close();
        }
    }
}
