import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javafx.util.Pair;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.*;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class UI {

	static Words[] words = new Words[55000];
	static List<Status> statuses = new ArrayList<Status>();
	static List<String> positiveStatuses = new ArrayList<String>();
	static List<Pair<Float, String>> Statuses = new ArrayList<Pair<Float, String>>();
	static List<String> negativeStatuses = new ArrayList<String>();
	static int noWords;

	private enum statusType {
		Positive, Negative;
	};

	public static void main(String[] args) {
		getWordsFromTrainingData();
		final Display display = new Display();

		Shell shell = new Shell(display);
		shell.setMaximized(true);
		shell.setText("Sentiment Analysis For Social Media Texts");
		Label searchKeywordLabel = new Label(shell, SWT.NONE);
		searchKeywordLabel.setText("Search Keyword: ");
		searchKeywordLabel.setBounds(5, 20, 150, 30);
		final Text searchKeyword = new Text(shell, SWT.BORDER | SWT.MULTI);
		searchKeyword.setBounds(150, 20, 150, 30);
		Label positiveTweetsLabel = new Label(shell, SWT.NONE);
		positiveTweetsLabel.setText("Positive: ");
		positiveTweetsLabel.setBounds(30, 60, 100, 30);
		final Label positiveTweetsScoreLabel = new Label(shell, SWT.NONE);
		positiveTweetsScoreLabel.setBounds(150, 60, 100, 30);
		final Text positiveTweets = new Text(shell, SWT.BORDER | SWT.MULTI);
		positiveTweets.setBounds(30, 90, 1250, 300);
		Label negativeTweetsLabel = new Label(shell, SWT.NONE);
		negativeTweetsLabel.setText("Negative: ");
		negativeTweetsLabel.setBounds(30, 400, 100, 30);
		final Label negativeTweetsScoreLabel = new Label(shell, SWT.NONE);
		negativeTweetsScoreLabel.setBounds(150, 400, 100, 30);
		final Text negativeTweets = new Text(shell, SWT.BORDER | SWT.MULTI);
		negativeTweets.setBounds(30, 430, 1250, 300);
		final Label resultLabel = new Label(shell, SWT.BOLD);
		resultLabel.setFont(new Font(display, "Arial", 20, SWT.BOLD));
		resultLabel.setText("Sentiment Analysis!!");
		resultLabel.setBounds(620, 20, 300, 50);
		Button button = new Button(shell, SWT.PUSH | SWT.BORDER);
		button.setBounds(400, 20, 100, 30);
		button.setText("Search");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Twitter twitter = TwitterFactory.getSingleton();
				Query query = new Query(searchKeyword.getText());
				query.lang("en");
				query.count(100);
				QueryResult result;
				try {
					result = twitter.search(query);
					for (Status status : result.getTweets()) {
						if (analyseStatus(status.getText()) == statusType.Positive) {
							positiveStatuses.add(status.getText() + "\n\n");
						} else {
							negativeStatuses.add(status.getText() + "\n\n");
						}
					}
					String temp = positiveStatuses.toString().replace(',', ' ');
					positiveTweets.setText(temp);
					positiveTweetsScoreLabel.setText(""
							+ positiveStatuses.size());
					temp = negativeStatuses.toString().replace(',', ' ');
					negativeTweets.setText(temp);
					negativeTweetsScoreLabel.setText(""
							+ negativeStatuses.size());
					int x = positiveStatuses.size() - negativeStatuses.size();
					if (x > 0) {
						resultLabel.setText("POSITIVE OVERALL!");
						resultLabel.setForeground(display
								.getSystemColor(SWT.COLOR_GREEN));
					} else if (x == 0) {
						resultLabel.setText("NEUTRAL!");
						resultLabel.setForeground(display
								.getSystemColor(SWT.COLOR_WHITE));
					} else {
						resultLabel.setText("NEGATIVE OVERALL!");
						resultLabel.setForeground(display
								.getSystemColor(SWT.COLOR_RED));
					}
					positiveStatuses.clear();
					negativeStatuses.clear();

				} catch (TwitterException e1) {
					e1.printStackTrace();
				}

			}
		});
		Button button1 = new Button(shell, SWT.PUSH | SWT.BORDER);
		button1.setBounds(1000, 20, 150, 30);
		button1.setText("Random Tweets");
		button1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				try {
					List<Status> result = getTwitterFeed.getFeed();
					for (Status status : result) {
						if (analyseStatus(status.getText()) == statusType.Positive) {
							positiveStatuses.add(status.getText() + "\n\n");
						} else {
							negativeStatuses.add(status.getText() + "\n\n");
						}
					}
					KMeans obj = new KMeans(Statuses);

					obj.performClustering();

					positiveStatuses = obj.getPositive();
					negativeStatuses = obj.getNegative();
					positiveTweets.setText("" + positiveStatuses);
					positiveTweetsScoreLabel.setText(""
							+ positiveStatuses.size());
					negativeTweets.setText("" + negativeStatuses);
					negativeTweetsScoreLabel.setText(""
							+ negativeStatuses.size());

					positiveStatuses.clear();
					negativeStatuses.clear();
					Statuses.clear();

				} catch (TwitterException e1) {
					e1.printStackTrace();
				}

			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void getWordsFromTrainingData() {

		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					"lexicons/unigrams-pmilexicon.txt"));
			String line;
			while ((line = reader.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(line, "\t");
				Words word = new Words();
				word.word = tokens.nextToken();
				word.score = Float.parseFloat(tokens.nextToken());
				words[noWords++] = word;
			}
			Arrays.sort(words, 0, noWords - 1);
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static statusType analyseStatus(String status) {
		StringTokenizer st = new StringTokenizer(status, " ");
		float score = 0;
		while (st.hasMoreTokens()) {
			String word = st.nextToken();
			score += findScoreInTrainingData(word);
		}
		Statuses.add(new Pair<Float, String>(score, status));
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
