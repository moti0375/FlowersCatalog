package com.bartovapps.flowerscatalog.data;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.Comparator;

/**
 * Created by BartovMoti on 01/07/15.
 */
public class Flower implements Comparable<Flower>{

    private int productId;
    private String name;
    private String category;
    private String instructions;
    private double price;
    private String imageUrl;
    private Bitmap flowerBitmap;

public Flower(){

    }

    public Flower(String name, String category, String instructions, double price, String imageUrl){
        setName(name);
        setCategory(category);
        setInstructions(instructions);
        setPrice(price);
        setImageUrl(imageUrl);
    }

    public Flower(Bundle b){
        if(b != null){
            this.productId = b.getInt("productId");
            this.name = b.getString("name");
            this.category = b.getString("category");
            this.instructions = b.getString("instructions");
            this.price = b.getDouble("price");
            this.imageUrl = b.getString("imageUrl");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int id) {
        this.productId = id;
    }

    public Bitmap getFlowerBitmap() {
        return flowerBitmap;
    }

    public void setFlowerBitmap(Bitmap flowerBitmap) {
        this.flowerBitmap = flowerBitmap;
    }

    public Bundle toBundle(){
        Bundle b = new Bundle();

        b.putInt("productId", this.productId);
        b.putString("name", this.name);
        b.putString("category", this.category);
        b.putString("instructions", this.instructions);
        b.putDouble("price", this.price);
        b.putString("imageUrl", this.imageUrl);

        return b;
    }


    @Override
    public int compareTo(Flower otherFlower) {
        return Comparators.NAME.compare(this, otherFlower);
    }


    public static class Comparators {

        public static Comparator<Flower> NAME = new Comparator<Flower>() {
            @Override
            public int compare(Flower flower1, Flower flower2) {
                return flower1.getName().compareTo(flower2.getName());
            }
        };
        public static Comparator<Flower> PRICE = new Comparator<Flower>() {
            @Override
            public int compare(Flower flower1, Flower flower2) {
                return (int)(flower1.getPrice() - flower2.getPrice());
            }
        };

    }
}


