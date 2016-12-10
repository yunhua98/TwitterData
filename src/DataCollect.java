import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import io.indico.Indico;
import io.indico.api.results.IndicoResult;
import io.indico.api.utils.IndicoException;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class DataCollect {
	
    static Indico indico;
	static int counter;
	static double numPositive;
	static PrintWriter writerActivity, writerLocation;
	
	static HashMap<String, Integer> countsP, countsN;
	static HashMap<String, Double> totalSentiment;
    
	public static void main(String[] args) throws TwitterException, IOException{
		// initialize static vars
		counter = 0;
		countsP = new HashMap<>();
		countsN = new HashMap<>();
		totalSentiment = new HashMap<>();
		
		// initialize Indico
		try {
			indico = new Indico(Config.INDICO_API_KEY);
		} catch (IndicoException e) {
			System.out.println("Connection to Indico failed.");
			e.printStackTrace();
		}
		
		// initialize twitter4j
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Config.TWITTER_API_KEY)
                .setOAuthConsumerSecret(Config.TWITTER_API_SECRET)
                .setOAuthAccessToken(Config.TWITTER_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(Config.TWITTER_ACCESS_SECRET);
        
        TwitterStream twitter = new TwitterStreamFactory(cb.build()).getInstance();
        FilterQuery filtro = new FilterQuery();    
        double[][] bb= {{-124.47, 24.0}, {-66.56, 49.3843}};
        filtro.locations(bb);
        
        try{
		    //writerActivity = new PrintWriter("tweetdata_activity_sentiment_wdate.txt", "UTF-8");
		    writerActivity = new PrintWriter("control.txt", "UTF-8");
		    //writerLocation = new PrintWriter("tweetdata_timezone.txt", "UTF-8");
		} catch (IOException e) {
			System.out.println("Write to file failed.");
			e.printStackTrace();
		}
		
	    StatusListener listener = new StatusListener(){
	        @Override
	    	public void onStatus(Status status) {
	        	String text = status.getText();
	        	User user = status.getUser();
	        	String timeZone = user.getTimeZone();
	        	
	        	if (true/*text.contains("Trump")*/) {
	        		++counter;
		        	
	        		IndicoResult single;
	        		double result = 0.5;
					
	        		try {
						single = indico.sentiment.predict(text);
						result = single.getSentiment();
					} catch (UnsupportedOperationException | IOException | IndicoException e) {
						// TODO Auto-generated catch block
						System.out.println("Getting sentiment analysis results failed.");
						e.printStackTrace();
					}
	        		
	        		/*if (timeZone != null) {
	        			if (result > 0.5) {
	        				if (countsP.containsKey(timeZone)) countsP.put(timeZone, countsP.get(timeZone) + 1);
	        				else countsP.put(timeZone, 1);
	        			}
	        			else {
	        				if (countsN.containsKey(timeZone)) countsN.put(timeZone, countsN.get(timeZone) + 1);
	        				else countsN.put(timeZone, 1);
	        			}
	        			if (totalSentiment.containsKey(timeZone)) totalSentiment.put(timeZone, totalSentiment.get(timeZone) + result);
	        			else totalSentiment.put(timeZone, result);
	        		}*/
	        		
	        		writerActivity.println(user.getStatusesCount() + "," + user.getCreatedAt().toString() + "," + result);
	        		
	        		System.out.println(counter + ": " + result + " - " + text);
	        	}
	        	
	        	if (counter >= 1700) {
        		    /*for (String s : totalSentiment.keySet()) {
	        			double positive = 0;
	        			double negative = 0;
	        			if (countsP.containsKey(s)) positive = countsP.get(s);
	        			if (countsN.containsKey(s)) negative = countsN.get(s);
	        			writerLocation.println(s + "," + totalSentiment.get(s) / (positive + negative) + "," + positive + "," + negative);
	        		}*/
	        		writerActivity.close();
	        		//writerLocation.close();
	        		System.exit(0);
	        	}
	        		
	        	
	            // System.out.println(status.getUser().getName() + " : " + status.getText());
	        }
	        @Override
	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
	        public void onException(Exception ex) {
	            ex.printStackTrace();
	        }
			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}
	    };
	    

        twitter.addListener(listener);
        twitter.filter(filtro);
	    
	    // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
	    twitter.sample();
	}

}
