package com.example.appbansach.modle;

public class Category {
    private String id;
    private String name;

//    private byte[] imgtheloai;

    public Category() {
        // Default constructor required for calls to DataSnapshot.getValue(Category.class)
    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public byte[] getImgtheloai() {
//        return imgtheloai;
//    }
//
//    public void setImgtheloai(byte[] imgtheloai) {
//        this.imgtheloai = imgtheloai;
//    }
}
