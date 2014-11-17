package com.example.zcapp;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockExpandableListActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;

public class HomeActivity extends SherlockExpandableListActivity {

	private ArrayList<String> parentItems = new ArrayList<String>();
	private ArrayList<Object> childItems = new ArrayList<Object>();
	private ArrayList<ProductCategory> categories;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// this is not really  necessary as ExpandableListActivity contains an ExpandableList
		setContentView(R.layout.activity_home);
		Intent splashScreenActivityIntent = getIntent();
		ActionBar actionbar = this.getSupportActionBar();
		actionbar.setTitle("Home");
		categories = (ArrayList<ProductCategory>) splashScreenActivityIntent.getSerializableExtra("PRODUCT_CATEGORIES");
		ExpandableListView expandableList = (ExpandableListView) findViewById(android.R.id.list); // you can use getExpandableListView()(

		expandableList.setDividerHeight(2);
		expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);

		setGroupParents();

		MyExpandableAdapter adapter = new MyExpandableAdapter(parentItems, categories);

		adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
		expandableList.setAdapter(adapter);
		expandableList.setOnChildClickListener(this);
	}

	public void setGroupParents() {
		parentItems.add("Products");
		parentItems.add("Contact Us");
		parentItems.add("Shipping Info");
		parentItems.add("FAQ");
	}

	
//	//need to c
//	public void setChildData() {
//
//		ArrayList<String> child = new ArrayList<String>();
//		
//		// Products
//		for(int i =0;i<categories.size();i++){
//			
//		}
//		//for(int i = 0;i<categories.size();i++)
//		//	child.add(categories.get(i).getName());
//		child.add("Fragrances"); //one default values
//		//child.add("Gssssames");
//		childItems.add(child);
//
//		// Core Java
//		child = new ArrayList<String>();
//		child.add("Contact Us");
//		childItems.add(child);
//
//		// Desktop Java
//		child = new ArrayList<String>();
//		child.add("Shipping Info");
//		childItems.add(child);
//
//		// Enterprise Java
//		child = new ArrayList<String>();
//		child.add("FAQ");
//		childItems.add(child);
//	}

}