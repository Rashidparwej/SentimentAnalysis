import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;
import java.net.*;
import java.io.*;

public class Testing {

	public static void main(String[] args) throws IOException {
		String fileName = "C:\\Users\\Pratik\\Desktop\\major\\example.csv"; // include the training data set name
											// here
		String[] data = new String[50000];
		String s, t, q, m;
		String[] positive = new String[50000];
		String[] negative = new String[50000];
		String[] sa = new String[50];
		String[][] test = new String[50][2];
		int j = 1, po = 1, ne = 1, k = 0;
		String[][] multi = new String[50000][50];
		String[][] multi1 = new String[50000][50];

		try {

			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String strLine = null;
			StringTokenizer st = null, x = null, y = null, p = null;
			int lineNumber = 0, tokenNumber = 0;

			while ((fileName = br.readLine()) != null) {
				lineNumber++;

				// break comma separated line using ";"
				st = new StringTokenizer(fileName, ";");

				while (st.hasMoreTokens()) {
					// display csv values
					tokenNumber++;
					s = st.nextToken();
					// System.out.println("Line # " + lineNumber + ", Token # "
					// + tokenNumber + ", Token : "+ s);
					if (tokenNumber == 6) {
						k = s.length();
						m = s.substring(2, k - 2);
						// System.out.println(m);
						data[lineNumber] = m;

					}

				}

				tokenNumber = 0;

			}


			for (int i = 1; i <= lineNumber; i++) {
				x = new StringTokenizer(data[i]);
				while (x.hasMoreTokens()) {

					t = x.nextToken();
					// System.out.println(t);
					multi[i][j] = t; // creating an array of individual words
					j++;
				}
				j = 1;
			}

			/*
			 * // displaying the contents of array for(int i=1; i<24;i++) {
			 * System.out.println("\nThe contents of record:"+i+" is\n");
			 * for(int k=1 ; k<multi[i].length;k++) { if(multi[i][k] == null)
			 * continue; else System.out.println(multi[i][k]); } //if(multi[i]
			 * == null) //break; }
			 */

			// Getting the Training data result from a file.
			lineNumber = -1;
			BufferedReader br1 = new BufferedReader(
					new FileReader("C:\\Users\\Pratik\\Desktop\\major\\result.txt"));
			while ((fileName = br1.readLine()) != null) {
				lineNumber++;

				// break comma separated line using " "
				p = new StringTokenizer(fileName, " ");
				int xy = 0;
				while (p.hasMoreTokens()) {

					q = p.nextToken();

					multi1[lineNumber][xy] = q;
					xy++;

				}

			}

			// comparing and clustering into +ve and -ve tweets

			int a1 = 1;
			int b1 = 0, c1 = 0;
			for (int a = 1; a < 20; a++) {
				a1 = 0;
				for (int b = 1; b < multi[a].length; b++) {
					if (multi[a][b] == null)
						break;
					else {

						for (int c = 1; c < multi1.length; c++) {
							if (multi1[c][0] == null) {
								break;
							} else {
								if (multi[a][b].equalsIgnoreCase(multi1[c][0]))

								{
									a1++;
									test[a1][0] = multi[a][b];
									test[a1][1] = multi1[c][3];
									break;
								}
							}
						}
					}
				}

				while (a1 > 0) {
					c1 = Integer.parseInt(test[a1][1]);
					b1+= c1;
					a1--;
				}
				if (b1 > 0) {

					int d1 = 1;
					String sa1 = "";
					while (multi[a][d1] != null) {
						sa1 = sa1 + " " + multi[a][d1];
						d1++;
					}
					positive[po] = sa1;
					po++;
				} else {
					int d1 = 1;
					String sa1 = "";

					while (multi[a][d1] != null) {

						sa1 = sa1 + " " + multi[a][d1];
						d1++;
					}
					negative[ne] = sa1;
					ne++;
				}

			}// end of for loop

			System.out.println("\n\nThe positive opinion Tweets are:\n\n");
			for (int i = 1; i < positive.length; i++) {
				if (positive[i] == null)
					break;
				else
					System.out.println(positive[i] + "\n");
			}
			System.out.println("\n\n\n\n");

			System.out.println("The negative opinion Tweets are:\n\n");
			for (int i = 1; i < positive.length; i++) {
				if (negative[i] == null)
					break;
				else
					System.out.println(negative[i] + "\n");

			}

		} // end of try block

		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
