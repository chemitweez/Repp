package chemmy.test4stqry;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends FragmentActivity {
    /**
     * The number of pages, will be initialized in onPostExecute
     * depending on the number of images fetched
     */
     int NUM_PAGES = 0; 
    List<Drawable> images; //List of our images

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter; 
    String url = "http://api.stqryv2.mars.stqry.com/search/entity?limit=10&keywords=Bob&sort=match&type=keyword&offset=0&entity_type=organization,story,exhibit&";
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new getImageTask().execute(url);
    }

    /**AsyncTask class implementing JSON parsing and fetching images
     * for the background
     */
    public class getImageTask extends AsyncTask<String, String, List<Bitmap>>{

		@Override
		protected List<Bitmap> doInBackground(String... params) {
			 
			JSONObject jobj = new JSONObject();
			JSONParser parser = new JSONParser();
			jobj = parser.justGet(params[0]);  
			
			List<String> imagesURL = new ArrayList<String>(); 
			
			try {		//Finding "low" field in "image_explore" in JSONArray "_links"		
				for(int i=0; i<3; i++){ 
				JSONArray jArr = jobj.getJSONArray("data");
				JSONObject jobj3 = jArr.getJSONObject(i).getJSONObject("_links");
				imagesURL.add(jobj3.getJSONObject("image_explore").get("low").toString());
				Log.i("1test", imagesURL.get(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			List<Bitmap> bitmaps = new ArrayList<Bitmap>();
			//Downloading bitmaps and saving in a list to send to onPostExe
			for(int i=0; i<imagesURL.size(); i++){	
			Bitmap bM = parser.getBitmapFromURL(imagesURL.get(i));
			bitmaps.add(bM);
			}
			return bitmaps;
		}
    	
		@Override
		protected void onPostExecute(List<Bitmap> result) {
		
			images = new ArrayList<Drawable>(); 
			//From Bitmap to Drawable
			for(int i=0; i<result.size(); i++){ 
				images.add(new BitmapDrawable(getResources(), result.get(i)));
			}
			NUM_PAGES = result.size(); //Finally initializing the total number of pages
	        mPager = (ViewPager) findViewById(R.id.pager);
	        mPager.setPageTransformer(true, new DepthPageTransformer());
	        //Sending List of Drawables to PagerAdapter
	        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), images);
	        mPager.setAdapter(mPagerAdapter);
	        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
	            @Override
	            public void onPageSelected(int position) {
	                invalidateOptionsMenu();
	            }
	        });
			super.onPostExecute(result);
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * A simple pager adapter that represents {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    	List<Drawable> images;
    	//Sending images to set as the backgrounds
        public ScreenSlidePagerAdapter(FragmentManager fm, List<Drawable> d) {
            super(fm);
            this.images = d;
        }



		@Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position, images);
        }
        
        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
