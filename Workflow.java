import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import twitter4j.Status;
import twitter4j.TwitterException;

public class Workflow {

	static Words[] words = new Words[55000];
	static List<Status> statuses = new ArrayList<Status>();
	static List<String> positiveStatuses = new ArrayList<String>();
	static List<String> negativeStatuses = new ArrayList<String>();
	static int noWords;

	private enum statusType {
		Positive, Negative;
	};

	public static void main(String[] args) {

		getWordsFromTrainingData();
		Arrays.sort(words,0,noWords-1);
		try {
			statuses = getTwitterFeed.getFeed();
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		clusteringProcess();

		System.out.println(positiveStatuses);
		System.out.println(negativeStatuses);
		/*
		 * // Getting the Text Data
		 * 
		 * // Separating Words String[][] multi = new String[1000][50];
		 * String[][] table = new String[5000][4]; for (int i = 1; i <= noLines;
		 * i++) { StringTokenizer x = new StringTokenizer(data[i - 1]); int j =
		 * 1; while (x.hasMoreTokens()) { String t = x.nextToken(); multi[i][j]
		 * = t; // creating an array of individual words j++; } }
		 * 
		 * 
		 * try { for (int c = 1; c < multi.length; c++) { for (int d = 1; d <
		 * multi[c].length; d++) { if (multi[c][d] == null ||
		 * multi[c][d].charAt(0) == '@' || multi[c][d].charAt(0) == '#')
		 * continue; else { // creating a connection to web.
		 * 
		 * // Searching the results in google for excellent // condition and
		 * storing them in a file
		 * 
		 * URL url = new URL( "http://www.google.com/search?q=excellent+" +
		 * multi[c][d]); URL url1 = new URL(
		 * "http://www.google.com/search?q=poor+" + multi[c][d]);
		 * 
		 * URLConnection conn = url.openConnection(); URLConnection conn1 =
		 * url1.openConnection();
		 * 
		 * conn.setRequestProperty( "User-Agent",
		 * "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)"
		 * );
		 * 
		 * BufferedReader in = new BufferedReader( new
		 * InputStreamReader(conn.getInputStream())); String str; int a = 0; //
		 * Create file FileWriter fstream = new FileWriter("out.txt");
		 * BufferedWriter out = new BufferedWriter(fstream);
		 * 
		 * while ((str = in.readLine()) != null) {
		 * 
		 * out.write(str);
		 * 
		 * a = a + 1;
		 * 
		 * }
		 * 
		 * in.close(); out.close();
		 * 
		 * // Searching the results in google for poor condition // and storing
		 * them in a file conn1.setRequestProperty( "User-Agent",
		 * "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)"
		 * );
		 * 
		 * BufferedReader in2 = new BufferedReader( new
		 * InputStreamReader(conn1.getInputStream())); String str2;
		 * 
		 * // Create file FileWriter fstream2 = new FileWriter("out1.txt");
		 * BufferedWriter out2 = new BufferedWriter(fstream2);
		 * 
		 * while ((str2 = in2.readLine()) != null) { out2.write(str2);
		 * 
		 * }
		 * 
		 * in2.close(); out2.close();
		 * 
		 * // Getting the search number of excellent condition and // storing in
		 * array
		 * 
		 * // Open the file that is the first // command line parameter
		 * FileInputStream fstream1 = new FileInputStream( "out.txt"); // Get
		 * the object of DataInputStream DataInputStream in1 = new
		 * DataInputStream(fstream1); BufferedReader br1 = new BufferedReader(
		 * new InputStreamReader(in1)); String strLine1, h, l; int i = 0, b = 0,
		 * k = 0; int y11 = 0, y1 = 1; // Read File Line By Line while
		 * ((strLine1 = br1.readLine()) != null) { // Print the content on the
		 * console b = strLine1.contains("About") ? 1 : 0; k =
		 * strLine1.indexOf("About"); i++; h = strLine1.substring(k + 6, k +
		 * 18);
		 * 
		 * StringTokenizer y = new StringTokenizer(h, " "); h = y.nextToken();
		 * 
		 * l = multi[c][d];
		 * 
		 * y11 = y1; table[y1][0] = l; table[y1][1] = h.replaceAll("\\D", "");
		 * y1++;
		 * 
		 * }
		 * 
		 * // Close the input stream in.close();
		 * 
		 * // Getting the search number of poor condition and // storing in
		 * array
		 * 
		 * // Open the file that is the first // command line parameter
		 * FileInputStream fstream21 = new FileInputStream( "out1.txt"); // Get
		 * the object of DataInputStream DataInputStream in21 = new
		 * DataInputStream(fstream21); BufferedReader br21 = new BufferedReader(
		 * new InputStreamReader(in21)); String strLine21, h21, l21; int i21 =
		 * 0, b21 = 0, k21 = 0; // Read File Line By Line while ((strLine21 =
		 * br21.readLine()) != null) { // Print the content on the console b21 =
		 * strLine21.contains("About") ? 1 : 0; k21 =
		 * strLine21.indexOf("About"); i21++; h21 = strLine21.substring(k21 + 6,
		 * k21 + 18);
		 * 
		 * StringTokenizer y21 = new StringTokenizer(h21, " "); h21 =
		 * y21.nextToken();
		 * 
		 * l21 = multi[c][d];
		 * 
		 * table[y11][2] = h21.replaceAll("\\D", "");
		 * 
		 * }
		 * 
		 * // Close the input stream in.close();
		 * 
		 * }
		 * 
		 * } }
		 * 
		 * FileWriter fresult = new FileWriter("result.txt"); BufferedWriter
		 * bresult = new BufferedWriter(fresult);
		 * 
		 * System.out.println("\nThe contents of table are:\n\n\n");
		 * 
		 * for (int ab = 0; ab < table.length; ab++) { if (table[ab + 1][0] ==
		 * null || table[ab + 1][0].length() == 0) { break; } else { String p =
		 * new String(table[ab + 1][1]); String q = new String(table[ab +
		 * 1][2]); int i5 = 1, i6 = 1;
		 * 
		 * for (int i2 = 0; i2 < p.length(); i2++) {
		 * 
		 * // If we find a non-digit character we return false. if
		 * (!Character.isDigit(p.charAt(i2))) { i5 = 0; break; } } for (int i2 =
		 * 0; i2 < q.length(); i2++) {
		 * 
		 * // If we find a non-digit character we return false. if
		 * (!Character.isDigit(q.charAt(i2))) { i6 = 0; break; } }
		 * 
		 * if (i5 == 0 || i6 == 0) { table[ab + 1][3] = Integer.toString(i5); }
		 * 
		 * else {
		 * 
		 * int e = Integer.parseInt(p); int f = Integer.parseInt(q);
		 * 
		 * table[ab + 1][3] = Integer.toString(e - f); for (int bc = 0; bc < 4;
		 * bc++)
		 * 
		 * { bresult.write(table[ab][bc]); bresult.write(" ");
		 * 
		 * System.out.print(table[ab][bc] + "     "); }
		 * System.out.println("\n"); bresult.write("\r\n"); } } }
		 * bresult.close(); } catch (NumberFormatException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch
		 * (MalformedURLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (FileNotFoundException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

	}

	private static void getWordsFromTrainingData() {

		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(
							"lexicons/unigrams-pmilexicon.txt"));
			String line;
			while ((line = reader.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(line, "\t");
				Words word = new Words();
				word.word = tokens.nextToken();
				word.score = Float.parseFloat(tokens.nextToken());
				words[noWords++] = word;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void clusteringProcess() {
		Iterator<Status> it = statuses.iterator();
		while (it.hasNext()) {
			Status s = it.next();
			statusType result = analyseStatus(s.getText());
			if (result == statusType.Positive) {
				positiveStatuses.add(s.getText());
			} else {
				negativeStatuses.add(s.getText());
			}
		}
	}

	public static statusType analyseStatus(String status) {
		StringTokenizer st = new StringTokenizer(status, " ");
		float score = 0;
		while (st.hasMoreTokens()) {
			String word = st.nextToken();
			score += findScoreInTrainingData(word);
		}
		if (score > 0) {
			return statusType.Positive;
		} else {
			return statusType.Negative;
		}
	}

	private static float findScoreInTrainingData(String word) {
		int lo = 0, hi = noWords - 1;
		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			int x = word.compareToIgnoreCase(words[mid].word);
			if (x == 0) {
				return words[mid].score;
			} else if (x < 0) {
				hi = mid - 1;
			} else {
				lo = mid + 1;
			}
		}
		return 0;
	}
}
