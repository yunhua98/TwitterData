import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Histogram {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		HashMap<Integer, Integer> total = new HashMap<>();
		HashMap<Integer, Integer> count = new HashMap<>();


		String path = "/Users/yunhuazhao/Documents/Java Programs/TwitterData/tweetdata_activity_sentiment.csv";
		for (String line : Files.readAllLines(Paths.get(path))) {
			String[] parts = line.split(",");
			int numTweets = (int) Double.parseDouble(parts[0]);
			double sentiment = Double.parseDouble(parts[1]);
			int bucket = (int) (sentiment * 10);
			if (numTweets < 100000) {
				if (total.containsKey(bucket)) {
					total.put(bucket, numTweets + total.get(bucket));
					count.put(bucket, count.get(bucket) + 1);
				}
				else {
					total.put(bucket, numTweets);
					count.put(bucket, 1);
				}
			}
		}

		PrintWriter w = new PrintWriter("tweetdata_timezone_histogram_reversed.txt", "UTF-8");
		for (int b : total.keySet()) w.println(b + "," + (total.get(b) / count.get(b)));
		w.close();

	}

}
