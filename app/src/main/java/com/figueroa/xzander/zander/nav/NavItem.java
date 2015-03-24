package com.figueroa.xzander.zander.nav;

/**
 * Created by xzander on 10/9/14.
 */
public class NavItem {

    private String title;
    private int icon;

    // constructor
    public NavItem(){

    }

    public NavItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }



}