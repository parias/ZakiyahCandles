package com.example.zcapp;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

public class MyExpandableAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private ArrayList<ProductCategory> childItems;
	private LayoutInflater inflater;
	private ArrayList<String> parentItems;
	
	public MyExpandableAdapter(ArrayList<String> parents, ArrayList<ProductCategory> childItems) {
		this.parentItems = parents;
		this.childItems = childItems;
	}
	
	//public MyExpandableAdapter(ArrayList<String> parents, ArrayList<ProductCategory> childern) {
	//	this.parentItems = parents;
	//	this.childtems = childern;
	//}

	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if(groupPosition == 0 ){
			final ProductCategory pc = (ProductCategory) childItems.get(childPosition);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.child, null);
			}
			convertView.setClickable(true);
			TextView textView = (TextView) convertView.findViewById(R.id.textView1);
			textView.setText(pc.getName());

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				//Change to get to new page, Toast will only show pop up message
				public void onClick(View view) {
					Intent productActivityIntent = new Intent(activity.getApplicationContext(), ProductActivity.class);
					productActivityIntent.putExtra("PRODUCT_CATEGORY", pc);
					activity.startActivity(productActivityIntent);
					
					//need to add the activity to android manifest.xml
					//to point to new page/activity with products
					//ie: <activity android:name=".Products"/>
				    //Intent intent = new Intent( );
				    //activity.startActivity(intent);
					
					
				}
			});
		}		

		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.group, null);
		}

		((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		//return 0;
		if (groupPosition == 0){
			return childItems.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return parentItems.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
