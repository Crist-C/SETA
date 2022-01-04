package com.silencedaemon.seta.MainMenu.vista;

public class ContenedorItemsMenu {

    private int iconItemMainMenu;
    private String tittleItemMainMenu;
    private Class activityItem;

    public ContenedorItemsMenu() {
    }
    public ContenedorItemsMenu(int iconItemMainMenu, String tittleItemMainMenu, Class activityItem) {
        this.iconItemMainMenu = iconItemMainMenu;
        this.tittleItemMainMenu = tittleItemMainMenu;
        this.activityItem = activityItem;
    }


    public int getIconItemMainMenu() {
        return iconItemMainMenu;
    }

    public void setIconItemMainMenu(int iconItemMainMenu) {
        this.iconItemMainMenu = iconItemMainMenu;
    }

    public String getTittleItemMainMenu() {
        return tittleItemMainMenu;
    }

    public void setTittleItemMainMenu(String tittleItemMainMenu) {
        this.tittleItemMainMenu = tittleItemMainMenu;
    }

    public Class getActivityItem() {
        return activityItem;
    }

    public void setActivityItem(Class activityItem) {
        this.activityItem = activityItem;
    }

}
