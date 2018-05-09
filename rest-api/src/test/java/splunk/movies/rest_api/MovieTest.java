package splunk.movies.rest_api;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

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
				setUpIsCompete = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testTest() {
		fail("Test");
	}
	
}