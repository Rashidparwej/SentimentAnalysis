import java.util.ArrayList;
import java.util.List;

import twitter4j.*;

public class getTwitterFeed {
	// List <String> tweets = new ArrayList<String>();
	public static List<Status> getFeed() throws TwitterException {
		Twitter twitter = TwitterFactory.getSingleton();
		Paging paging = new Paging(1,100);
		List<Status> statuses = twitter.getHomeTimeline(paging);
		return statuses;
	}
}
