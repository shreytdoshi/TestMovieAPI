package splunk.movies.rest_api;

import java.awt.image.BufferedImage;

public class Movie {
	
	private int id;
	private String title;
	private String imageURL;
	private BufferedImage image;
	private int [] genreIDs;
	private int genreSum;
	private boolean hasPalindrome;
	
	public Movie(int id, String title, String imageURL, BufferedImage image, int[] genreIDs, int genreSum,
			boolean hasPalindrome) {
		super();
		this.id = id;
		this.title = title;
		this.imageURL = imageURL;
		this.image = image;
		this.genreIDs = genreIDs;
		this.genreSum = genreSum;
		this.hasPalindrome = hasPalindrome;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public int[] getGenreIDs() {
		return genreIDs;
	}
	public void setGenreIDs(int[] genreIDs) {
		this.genreIDs = genreIDs;
	}
	public int getGenreSum() {
		return genreSum;
	}
	public void setGenreSum(int genreSum) {
		this.genreSum = genreSum;
	}
	public boolean isHasPalindrome() {
		return hasPalindrome;
	}
	public void setHasPalindrome(boolean hasPalindrome) {
		this.hasPalindrome = hasPalindrome;
	}	
}
