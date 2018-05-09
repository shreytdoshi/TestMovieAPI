package splunk.movies.rest_api;

import java.awt.image.BufferedImage;
//import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

//import javax.imageio.ImageIO;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class MovieTest extends TestCase {

	private static Movie [] movies;
	private static boolean setUpIsCompete;

	/*SetUp for all Test Cases*/
	@Before
	public void setUp(){
		if(!setUpIsCompete) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				String url = "https://splunk.mocklab.io/movies?q=batman";
				HttpGet httpGet = new HttpGet(url);
				httpGet.addHeader("Accept", "application/json");
				ResponseHandler<String> responseHandler=new BasicResponseHandler();
				String responseBody = httpclient.execute(httpGet, responseHandler);
				System.out.println(responseBody);

				JSONObject response = new JSONObject(responseBody);

				JSONArray res = response.getJSONArray("results");
				int len = res.length();
				movies = new Movie[len];

				for(int i = 0; i < len; i++) {
					JSONObject obj = res.getJSONObject(i);

					int id = obj.getInt("id");

					String title = obj.getString("title");
					String imageURL = obj.optString("poster_path");

					//TODO: Store Image 
					BufferedImage image = null;
					/*
					if(isValid(imageURL)) {
						URL u = new URL(imageURL);
						InputStream is = u.openStream();
						ImageIO.read(is);
					}*/

					JSONArray gIds = obj.getJSONArray("genre_ids");
					int gLen =  gIds.length();
					int [] genreIDs = new int [gLen];
					int genreSum = 0;
					for(int j = 0; j < gLen; ++j) {
						genreIDs[j] = gIds.getInt(j);
						genreSum += genreIDs[j];
					}

					boolean hasPalindrome = false;

					String [] words = title.split(" ");
					for(String word : words) {
						if(isPalindrome(word)) {
							hasPalindrome = true;
							break;
						}
					}
					Movie movie = new Movie(id, title, imageURL, image, genreIDs, genreSum, hasPalindrome);
					movies[i] = movie;
				}

				setUpIsCompete = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isPalindrome(String s) {
		String x = s.toLowerCase();
		char [] c = x.toCharArray();
		for(int i = 0; i < c.length/2; i++) {
			if(c[i] != c[c.length-1-i]) {
				return false;
			}
		}
		return true;
	}

	public boolean isValid(String url){
		try {
			new URL(url).toURI();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	/*
	 * Test Case 1 for BR-SPL-001: No two movies should have the same image
	*/
	
	@Test
	public void testDuplicateImageURLs() {
		Set<String> set = new HashSet<String>();
		for(Movie mov : movies) {
			String imageURL = mov.getImageURL();
			if(set.contains(imageURL)){
				fail("Two movies have the same Poster Path - " + imageURL);
			}else {
				set.add(imageURL);
			}
		}
	}

}