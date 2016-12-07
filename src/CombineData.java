import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class CombineData {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		HashMap<String, Double> countsP = new HashMap<>();
		HashMap<String, Double> countsN = new HashMap<>();
		HashMap<String, Double> totalSentiment = new HashMap<>();
		
		for (int i = 1; i < 10; ++i) {
			String path = "/Users/yunhuazhao/Documents/Java Programs/TwitterData/tweetdata_timezone_test" + i + ".txt";
			for (String line : Files.readAllLines(Paths.get(path))) {
				String[] parts = line.split(",");
				String timeZone = parts[0]; double avg = Double.parseDouble(parts[1]); double numP = Double.parseDouble(parts[2]); double numN = Double.parseDouble(parts[3]);
				//if (!timeZone.contains("(US & Canada)")) timeZone = "Other";
				if (totalSentiment.containsKey(timeZone)) totalSentiment.put(timeZone, totalSentiment.get(timeZone) + avg * (numP + numN));
				else totalSentiment.put(timeZone, avg * (numP + numN));
				if (countsP.containsKey(timeZone)) countsP.put(timeZone, countsP.get(timeZone) + numP);
				else countsP.put(timeZone, numP);
				if (countsN.containsKey(timeZone)) countsN.put(timeZone, countsN.get(timeZone) + numN);
				else countsN.put(timeZone, numN);
			}
		}
		
		PrintWriter w = new PrintWriter("tweetdata_timezone_combined.txt", "UTF-8");
		for (String s : totalSentiment.keySet()) w.println(s + "," + (totalSentiment.get(s) / (countsP.get(s) + countsN.get(s)))+ "," + countsP.get(s) + "," + countsN.get(s));
		w.close();
	}

}
