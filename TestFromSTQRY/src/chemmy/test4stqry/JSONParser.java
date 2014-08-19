package chemmy.test4stqry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class JSONParser {
	  static InputStream is = null;
	  static JSONObject jObj = null;
	  static String json = "";
	  StringBuilder sb;
	  String result;
	
	  public JSONParser() {
	  }
	  
	  /**Decodes InpustStream to String
	   * @return String
	   */
	  public static String convertStreamToString(InputStream is) 
	  {
	      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	      StringBuilder sb = new StringBuilder();

	      String line = null;
	      try 
	      {
	          while ((line = reader.readLine()) != null) 
	          {
	              sb.append(line + "\n");
	          }
	      } 
	      catch (Exception e) 
	      {
	          e.printStackTrace();
	      } 
	      finally 
	      {
	          try 
	          {
	              is.close();
	          } 
	          catch (Exception e) 
	          {
	              e.printStackTrace();
	          }
	      }
	      return sb.toString();
	  }
	  
	  /**Downloads image form Url and decodes received Bitmap
	   * @param link
	   * @return Bitmap
	   */
	  public Bitmap getBitmapFromURL(String link) {

		    try {
		        URL url = new URL(link);
		        HttpURLConnection connection = (HttpURLConnection) url
		                .openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        Bitmap myBitmap = BitmapFactory.decodeStream(input);

		        return myBitmap;

		    } catch (IOException e) {
		        e.printStackTrace();
		        Log.e("getBmpFromUrl error: ", e.getMessage().toString());
		        return null;
		    }
		}
	  
	  /**Fetches json obj from Url using HttpGet (yep)
	   * @return JSON Object
	   */
	  public JSONObject justGet(String url){
		  JSONObject jobj = new JSONObject();
		  String str="initial string"; 
		  try{
		  DefaultHttpClient httpClient = new DefaultHttpClient();

          HttpGet httpGet = new HttpGet(url);

          HttpResponse httpResponse = httpClient.execute(httpGet);
          HttpEntity httpEntity = httpResponse.getEntity();

          is = httpEntity.getContent();
		  }catch (Exception e){
			  e.printStackTrace();
		  }
		  str = convertStreamToString(is);
		  
		try {
			jobj = new JSONObject(str);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		  return jobj;
	  }
	
	}
