package com.example.zcapp;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductCategory implements Serializable {
	String name;
	String categoryURL;
	ArrayList<Product> categoryProducts;
	
	public ProductCategory(String name, String categoryURL, ArrayList<Product> categoryProducts){
		this.name = name;
		this.categoryURL = categoryURL;
		this.categoryProducts = categoryProducts;
	}
	
	public ProductCategory(){
		this.name = "";
		this.categoryURL = "";
		this.categoryProducts = new ArrayList<Product>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryURL() {
		return categoryURL;
	}

	public void setCategoryURL(String categoryURL) {
		this.categoryURL = categoryURL;
	}

	public ArrayList<Product> getCategoryProducts() {
		return categoryProducts;
	}

	public void setCategoryProducts(ArrayList<Product> categoryProducts) {
		this.categoryProducts = categoryProducts;
	}
}
