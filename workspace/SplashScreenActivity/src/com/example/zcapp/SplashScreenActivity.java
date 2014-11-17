package com.example.zcapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class SplashScreenActivity extends SherlockActivity implements OnClickListener {
	ProgressBar loading;
	Button buttonRetry;
	TextView textViewErrorMessage;
	ArrayList<ProductCategory> categories;
	boolean internetConnection;
	private final Object lock = new Object();
	private int productCount;
	private int productTotal;
	private Context mContext;
	//Method Called on Activity Creations
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_NoActionBar);
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.activity_splash_screen);
		}catch(Exception e1){
			System.out.println(e1.getMessage());
		}
		
		
		//Initialize UI objects
		loading = (ProgressBar) findViewById(R.id.progressBar1);
		buttonRetry = (Button) findViewById(R.id.buttonRetry);
		buttonRetry.setOnClickListener(this);
		textViewErrorMessage = (TextView) findViewById(R.id.textViewErrorMessage);
		
		ActionBar actionbar = this.getSupportActionBar();
		
		//ArrayList will be populated by background thread and passed to next Activity with product information
		categories = new ArrayList<ProductCategory>();
		internetConnection = true;
		productCount = 0;
		productTotal = 0;
		
		mContext = getApplicationContext();
		
		
        
		
		//Execute background thread to get Candle category page URLs
		populate();
		@SuppressWarnings("unused")
		String me = "";
	}
	
	//Gets URLs to different candle categories
	private class GetCategories extends AsyncTask<Void,Void, Document>{
		String homePageURL;
		public GetCategories(String homePageURL) {
			   this.homePageURL = homePageURL;
		}
		
		@Override
		protected Document doInBackground(Void... params)  {
			// TODO Auto-generated method stub
			Document homePage = null;
			//Try to connect to Zakiyah Candles Webpage
			try {
				homePage = Jsoup.connect(homePageURL).timeout(10*1000).get();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				internetConnection = false;
				Log.w("myApp", e1.getMessage());
			}
			if(internetConnection){
				//Search HTML for pertinent data
				Elements subMenu = homePage.select(".subMenu a");
				List<Element> subMenuArray = subMenu.subList(0, subMenu.size());
				for(int i=0; i<subMenuArray.size();i++){
					ProductCategory pc = new ProductCategory();
					pc.setName(subMenuArray.get(i).text());
					pc.setCategoryURL(subMenuArray.get(i).attr("href"));
					categories.add(pc);
				}
			}
			
			return homePage;
		}
		
		@Override
		protected void onPostExecute(Document homePage) {
			//If connection successful add found Candle Categories
			if(internetConnection==true){
				//ArrayList<GetProducts> getProductsTasks = new ArrayList<GetProducts>();
				for(int i=0;i< categories.size();i++){
					//upon declaring and instantiating ProductCategory execute background
					//thread to find Product URLs in that category
					//GetProducts productsTask = (GetProducts) new GetProducts(categories.get(i)).execute();
					//getProductsTasks.add(productsTask);
					//productsTask.execute();
					new GetProducts(categories.get(i)).execute();
				}
				
//				Context context = getApplicationContext();
//				CharSequence text = "GetCategories Completed!";
//				int duration = Toast.LENGTH_SHORT;
//
//				Toast toast = Toast.makeText(context, text, duration);
//				toast.show();
			}else{// If no internet connection display Error message, hide progress bar, and display retry button
				loading.setVisibility(View.GONE);
				buttonRetry.setVisibility(View.VISIBLE);
				textViewErrorMessage.setVisibility(View.VISIBLE);
			}
			
			
		}
	}
	
	//Class get product page URLs to each product in category
	private class GetProducts extends AsyncTask<Void,Void, Document>{
		ProductCategory category;
		public GetProducts(ProductCategory category) {
			   this.category = category;
		}
		
		//Try to connect to product category page and get Product page URLs
		@Override
		protected Document doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String categoryPageURL = category.getCategoryURL().replace("#!", "?_escaped_fragment_=");
			Document productsPage = null;
			try {
				productsPage = Jsoup.connect(categoryPageURL).timeout(10*1000).get();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				internetConnection = false;
				Log.w("myApp", e1.getMessage());
			}
			if(internetConnection){
				Elements subMenu = productsPage.select(".SeoAppPart a");
				List<Element> subMenuArray = subMenu.subList(0, subMenu.size());
				for(int i=0; i<subMenuArray.size();i++){
					Product p = new Product();
					p.setURL(subMenuArray.get(i).attr("href"));
					category.getCategoryProducts().add(p);
				}
				synchronized(lock){
					productTotal += category.getCategoryProducts().size();
				}
			}
			return productsPage;
		}
		
		@Override
		protected void onPostExecute(Document productsPage) {
			//If internet connection create product with product URL and add to category
			if(internetConnection){
				ArrayList<Product> products = category.getCategoryProducts();
				for(int j=0;j<products.size();j++){
					new GetProduct(products.get(j)).execute();
					
					//getProductTask.execute();
				}
				
				
//				Context context = getApplicationContext();
//				CharSequence text = "got products!";
//				int duration = Toast.LENGTH_SHORT;
//
//				Toast toast = Toast.makeText(context, text, duration);
//				toast.show();
			}else{	// If no internet connection display Error message, hide progress bar, and display retry button
				loading.setVisibility(View.GONE);
				buttonRetry.setVisibility(View.VISIBLE);
				textViewErrorMessage.setVisibility(View.VISIBLE);
			}
		}
	}
		
	//Connect to a Product's page and get the details
	public class GetProduct extends AsyncTask<Void,Void, Document>{
		Product product;
		public GetProduct(Product product){
			this.product = product;
		}
		//Try to connect to Product page and get product details
		@Override
		protected Document doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String productPageURL = product.getURL().replace("#!", "?_escaped_fragment_=");
			Document productPage = null;
			try {
				productPage = Jsoup.connect(productPageURL).timeout(10*1000).get();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				internetConnection = false;
				Log.w("myApp", e1.getMessage());
			}
			if(internetConnection){
				Elements subMenu = productPage.select(".SeoAppPart span, h4, img[title!=\"\"]");
				List<Element> subMenuArray = subMenu.subList(0, subMenu.size());
				product.setName(subMenuArray.get(2).text());
				if(product.getName().toLowerCase(Locale.getDefault()).contains("wax tart")){
					product.setPrice(subMenuArray.get(5).text());
					product.setDimensions("");
					product.setName("Wax Tart");
				}else{
					product.setDimensions(subMenuArray.get(5).text());
					product.setPrice(subMenuArray.get(4).text());
				}
				product.setImageURL(subMenuArray.get(0).attr("src"));
				product.setDescription(subMenuArray.get(3).text());
			}
			return productPage;
		}
		
		@Override
		protected void onPostExecute(Document result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(internetConnection){
				
				//try{
				
//					product.setImage(imageLoader.loadImageSync(product.getImageURL()));
//					synchronized(lock){
//						productCount += 1;
//					}
//					checkIfDone();
//				
				//If internet connection populate product with its details
				synchronized(lock){
					productCount += 1;
				}
				checkIfDone();
				
			}else{// If no internet connection display Error message, hide progress bar, and display retry button
				loading.setVisibility(View.GONE);
				buttonRetry.setVisibility(View.VISIBLE);
				textViewErrorMessage.setVisibility(View.VISIBLE);
			}
			
		}
		
	}
	
	private void checkIfDone(){
		synchronized(lock){
			if(productCount == productTotal){
//				Context context = getApplicationContext();
//				CharSequence text = "FINISHED!";
//				int duration = Toast.LENGTH_SHORT;
//
//				Toast toast = Toast.makeText(context, text, duration);
//				toast.show();
				
				Intent homeActivityIntent = new Intent(mContext, HomeActivity.class);
				homeActivityIntent.putExtra("PRODUCT_CATEGORIES", categories);
				//productActivityIntent.putExtra("CATEGORY_URL", productURLs.get(j));
				startActivity(homeActivityIntent);
			}
		}
		
	}
				
			
	//Event handling for retry button if no internet connection
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		loading.setVisibility(View.VISIBLE);
		buttonRetry.setVisibility(View.GONE);
		textViewErrorMessage.setVisibility(View.GONE);
		synchronized(lock){
			internetConnection = true;
			productTotal = 0;
			productCount = 0;
		}
		populate();
	}
	
	public void populate(){
		try {
			//Get home page string from Strings.xml
			String homePageURL = getResources().getString(R.string.homePageURL);
			
			//new GetCategories(homePageURL).execute().get();
			new GetCategories(homePageURL).execute();
			//task.execute();
			if(internetConnection == false){
				Context context = getApplicationContext();
				CharSequence text = "internet connection fail!";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				return;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			loading.setVisibility(View.GONE);
			buttonRetry.setVisibility(View.VISIBLE);
			textViewErrorMessage.setVisibility(View.VISIBLE);
			e.printStackTrace();
		}
	}

}
