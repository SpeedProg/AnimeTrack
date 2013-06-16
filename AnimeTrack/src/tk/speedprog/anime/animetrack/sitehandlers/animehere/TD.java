package tk.speedprog.anime.animetrack.sitehandlers.animehere;

public class TD {
	private int start, end;
	private String content;
	public TD(int s, int e, String content) {
		this.content = content;
		this.start = s;
		this.end = e;
		System.out.println("Start: " + this.start + " End: " + this.end
				+ " Content: " + this.content);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
