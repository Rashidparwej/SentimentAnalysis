import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.util.Pair;

public class KMeans {

	List<Pair<Float, String>> statuses;
	List<Pair<Float, String>> positive;
	List<Pair<Float, String>> negative;
	float positiveMean, negativeMean;

	public KMeans(List<Pair<Float, String>> statuses) {
		this.statuses = statuses;
	}

	public List<String> getPositive() {
		List<String> ret = new ArrayList<String>();
		for (Pair<Float, String> str : positive) {
			ret.add(str.getValue());
		}
		return ret;
	}

	public List<String> getNegative() {
		List<String> ret = new ArrayList<String>();
		for (Pair<Float, String> str : negative) {
			ret.add(str.getValue());
		}
		return ret;
	}

	private Float distance(float a, float b) {
		return Math.abs(a - b);
	}

	public void performClustering() {
		selectMeansAtRandom();
		while (true) {
			for (Pair<Float, String> p : positive) {
				if (distance(p.getKey(), negativeMean) < distance(p.getKey(),
						positiveMean)) {
					positive.remove(p);
					negative.add(p);
				}
			}
			for (Pair<Float, String> p : negative) {
				if (distance(p.getKey(), negativeMean) > distance(p.getKey(),
						positiveMean)) {
					positive.add(p);
					negative.remove(p);
				}
			}
			Float newPositiveMean = (float) 0.0;
			for (Pair<Float, String> p : positive) {
				newPositiveMean += p.getKey();
			}
			newPositiveMean /= positive.size();
			Float newNegativeMean = (float) 0.0;
			for (Pair<Float, String> p : negative) {
				newNegativeMean += p.getKey();
			}
			newNegativeMean /= negative.size();
			if (newPositiveMean == positiveMean
					&& newNegativeMean == negativeMean) {
				break;
			} else {
				negativeMean = newNegativeMean;
				positiveMean = newPositiveMean;
			}
		}
	}

	private void selectMeansAtRandom() {
		List<Float> scores=new ArrayList<Float>();
		for(Pair<Float,String> p: statuses){
			scores.add(p.getKey());
		}
		Arrays.sort(scores);
	}

}
