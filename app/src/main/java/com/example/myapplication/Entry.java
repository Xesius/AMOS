package com.example.myapplication;
public class Entry {



    private String pass;
    private String name;
    private String desc;

    public Entry(String name, String desc, String pass) {

        this.name = name;
        this.desc = desc;
        this.pass = pass;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}