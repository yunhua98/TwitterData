import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParse {

	public static void main(String[] args) throws ParseException, IOException {
		// TODO Auto-generated method stub

		Date today = new Date();
		SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		PrintWriter w = new PrintWriter("tweetdata_account_age.csv", "UTF-8");

		String path = "/Users/yunhuazhao/Documents/Java Programs/TwitterData/tweetdata_activity_sentiment_wdate.csv";
		for (String line : Files.readAllLines(Paths.get(path))) {
			String[] parts = line.split(",");
			double sentiment = Double.parseDouble(parts[2]);
			Date d = parser.parse(parts[1]);
			int days = (int) ((today.getTime() - d.getTime()) / 86400000);
			w.println(days + "," + sentiment);
		}

		w.close();
	}

}
