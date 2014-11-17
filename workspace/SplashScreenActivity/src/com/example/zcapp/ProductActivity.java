package com.example.zcapp;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ProductActivity extends SherlockActivity {

	private ProductCategory category;
	private ListView listView;
	private Activity activity;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		Intent productActivityIntent = getIntent();
		category = (ProductCategory) productActivityIntent.getSerializableExtra("PRODUCT_CATEGORY");
		ActionBar actionbar = this.getSupportActionBar();
		actionbar.setTitle(category.getName());
		activity = this;
		listView = (ListView) findViewById(android.R.id.list);
		mContext = this.getApplicationContext();
		listView.setAdapter(new IconicAdapter());
	}
	
	class IconicAdapter extends BaseAdapter {
		private LayoutInflater inflater = null;
		private ImageLoader imageLoader;
		
		IconicAdapter() {
			inflater=( LayoutInflater ) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//Initialize imageloader essential variables
			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		        .cacheInMemory(false)
		        .cacheOnDisk(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.EXACTLY)
		        .build();
					
	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
		        .defaultDisplayImageOptions(defaultOptions)
		        .diskCacheExtraOptions(100, 100, null)
		        .threadPoolSize(2)
		        .build();
	        
	        imageLoader = ImageLoader.getInstance();
	        imageLoader.init(config);
		}
		
		public View getView(int position, View convertView,ViewGroup parent) {
			LayoutInflater inflater=getLayoutInflater();
			View row=inflater.inflate(R.layout.row, parent, false);
			
			Product p = category.getCategoryProducts().get(position);
			
			final ImageView imgProduct=(ImageView)row.findViewById(R.id.imgProductPicture);
			//Load image, decode it to Bitmap and return Bitmap to callback
			ImageSize targetSize = new ImageSize(150, 150); // result Bitmap will be fit to this size
			imageLoader.displayImage(p.getImageURL(),imgProduct);
//			imageLoader.loadImage(p.getImageURL(),targetSize, new SimpleImageLoadingListener() {
//			    @Override
//			    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			    	// Do whatever you want with Bitmap
//			    	imgProduct.setImageBitmap(loadedImage);
//			    	imgProduct.refreshDrawableState();
//			    	Context context = getApplicationContext();
//					CharSequence text = "got a product!";
//					int duration = Toast.LENGTH_SHORT;
//
//					Toast toast = Toast.makeText(context, text, duration);
//					toast.show();        
//			    }
//			});
			//Bitmap bitmap = products.get(position).getImageURL();
			//bitmap = Bitmap.createScaledBitmap(bitmap, 96, 96, false);
			//icon.setImageBitmap(bitmap);
			
			TextView productName=(TextView)row.findViewById(R.id.txtProductName);
			TextView productPrice=(TextView)row.findViewById(R.id.txtProductPrice);
			
			productName.setText(p.getName());
			productPrice.setText(p.getPrice());
			
			return(row);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return category.getCategoryProducts().size();
		}

		@Override
		public Product getItem(int position) {
			// TODO Auto-generated method stub
			return category.getCategoryProducts().get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.product, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_product,
//					container, false);
//			return rootView;
//		}
//	}

}
