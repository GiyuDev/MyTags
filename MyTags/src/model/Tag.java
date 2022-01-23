package model;

public class Tag {

    private String name;
    private String symbol;
    private String permissionNode;

    public Tag(String name, String symbol, String permissionNode) {
        this.name = name;
        this.symbol = symbol;
        this.permissionNode = permissionNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    public void setPermissionNode(String permissionNode) {
        this.permissionNode = permissionNode;
    }
}
