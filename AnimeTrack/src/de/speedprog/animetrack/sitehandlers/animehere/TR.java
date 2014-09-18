package de.speedprog.animetrack.sitehandlers.animehere;

public class TR {

	private int start, end;
	private String content;

	public TR(int s, int e, String content) {
		this.content = content;
		this.start = s;
		this.end = e;
		System.out.println("Start: " + this.start + " End: " + this.end
				+ " Content: " + this.content);
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
