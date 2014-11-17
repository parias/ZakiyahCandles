package com.example.zcapp;

import java.io.Serializable;

import android.graphics.Bitmap;


public class Product implements Serializable{
	private String name;
	private String price;
	private String URL;
	private Bitmap image;
	private String imageURL;
	private String description;
	private String dimensions;
	public Product() {
		// TODO Auto-generated constructor stub
		name = "";
		price = "";
		setImage(null);
	}
	
	public Product(String name, String price, Bitmap imageURL){
		this.name = name;
		this.price = price;
		this.setImage(imageURL);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the imageURL
	 */
	public Bitmap getImage() {
		return image;
	}
	
	public void setImage(Bitmap image) {
		// TODO Auto-generated method stub
		this.image = image;
	}

	/**
	 * @param imageURL the imageURL to set
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public String getImageURL() {
		return imageURL;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

}
