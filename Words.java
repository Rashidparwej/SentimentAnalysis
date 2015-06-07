
public class Words implements Comparable{
	public String word;
	public float score;
	@Override
	public int compareTo(Object o) {
		int x= this.word.compareToIgnoreCase(((Words)o).word);
		return x;
	}
}
