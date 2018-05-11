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
	 * Test Case 1 for BR SPL-001: No two movies should have the same image
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
	
	
	/*
	 * Test Case for BR SPL-002: All poster_path links must be valid.
	 * poster_path link of null is also acceptable
	*/
	
	@Test
	public void testIfPosterPathsAreValid() {
		for(Movie mov : movies) {
			String imageURL = mov.getImageURL();
			if(imageURL != null && !isValid(imageURL)) {
				fail("Poster Path is not valid: "+ imageURL);
			}
		}
	}
	
	/*
	 * Test Case for BR SPL-004: 
	 * The number of movies whose sum of "genre_ids" > 400 should be no more than 7
	*/
	
	@Test
	public void testIfGenreSum400LessThan7() {
		int count = 0;
		for(Movie mov : movies) {
			if(mov.getGenreSum() > 400) {
				count++;
			}
		}
		if(count > 7) {
			fail("There are more than 7 movies with a Genre Sum greater than 400");
		}
	}

	/*
	 * Test Case for BR SPL-005: 
	 * There is at least one movie in the database whose title has a palindrome in it.
	*/
	 
	@Test
	public void testIfAtLeastOneMovieWithPalindrome() {
		int count = 0;
		for(Movie mov : movies) {
			if(mov.doesHavePalindrome()) {
				count++;
			}
		}
		if(count < 1) {
			fail("There is no movie in the database with a palindrome in it's title");
		}
	}
	
	/*
	 * Test Case for BR SPL-006: 
	 * There are at least two movies in the database whose title contain the title of another movie.
	*/	
	
	@Test
	public void testIfSubstringCountIsAtLeastTwo() {
		int count = 0;
		for(int i = 0; i < movies.length; ++i) {
			for(int j = i+1; j < movies.length; ++j) {
				String t1 = movies[i].getTitle();
				String t2 = movies[j].getTitle();
				if(t1.contains(t2) || t2.contains(t1)) {
					count++;
				}
			}
		}
		if(count < 2) {
			fail("There are less than two set of movies in the database whose title contain the title of another movie.");
		}
	}
	
	


}