package serp;

import serp.rbtree.Color;

/**
 * Class to represent web page. Field score is overall score to rank web page in
 * search engine. Field score calculates from another 4 fields: wordFrequency,
 * daysExisted, numberOfLinks, moneyPayed.
 * 
 * @author msurmenok
 *
 */
public class WebPage implements RankedElement
{
	private String link;
	private int wordFrequency;
	private int daysExisted;
	private int numberOfLinks;
	private int adMoney;
	public Color color;

	private int score; // final score of the web page that depending from 4 other features.

	private int index; // position in heap


	/**
	 * Construct an instance of Web Page and calculate overall score from 4
	 * parameters.
	 * 
	 * @param link
	 *            web page address.
	 * @param wordFrequency
	 *            score for the frequency and location of keywords within the Web
	 *            page.
	 * @param daysExisted
	 *            score for how long the Web page has existed.
	 * @param numberOfLinks
	 *            the number of other Web pages that link to the page in question.
	 * @param moneyPayed
	 *            how much the webpage owner has paid to Google for advertisement
	 *            purpose.
	 * @param index
	 *            current position in list.
	 * 
	 */
	public WebPage(String link, int wordFrequency, int daysExisted, int numberOfLinks, int moneyPayed, int index)
	{
		this.link = link;
		this.wordFrequency = wordFrequency;
		this.daysExisted = daysExisted;
		this.numberOfLinks = numberOfLinks;
		this.adMoney = moneyPayed;
		this.index = index;
		updateScore();
	}


	/**
	 * Calculate and update overall score based on 4 parameters: wordFrequency,
	 * daysExisted, numberOfLinks, and moneyPayed.
	 */
	private void updateScore()
	{
		this.score = this.wordFrequency + this.daysExisted + this.numberOfLinks + adMoney;
	}


	/**
	 * Accessor for score field.
	 * 
	 * @return final score for page ranking.
	 */
	public int getScore()
	{
		return this.score;
	}


	/**
	 * Accessor for link field.
	 * 
	 * @return web address of the page.
	 */
	public String getLink()
	{
		return this.link;
	}


	/**
	 * Update position of web page in the heap.
	 * 
	 * @param index
	 *            new position in array.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}


	/**
	 * Accessor for index field.
	 * 
	 * @return the current position in the heap.
	 */
	public int getIndex()
	{
		return this.index;
	}


	/**
	 * Update new frequency of keyword if it is greater than previous value and
	 * recalculate final score.
	 * 
	 * @param wordFrequency
	 */
	public void setWordFrequency(int wordFrequency)
	{
		if (wordFrequency > this.wordFrequency)
		{
			this.wordFrequency = wordFrequency;
			updateScore();
		}

	}


	/**
	 * Accessor for wordFrequency field.
	 * 
	 * @return the number of keywords on page.
	 */
	public int getWordFrequency()
	{
		return this.wordFrequency;
	}


	/**
	 * Update how many days the web page is existed if the new value is greater than
	 * original and recalculate final score.
	 * 
	 * @param wordFrequency
	 */
	public void setDaysExisted(int daysExisted)
	{
		if (daysExisted > this.daysExisted)
		{
			this.daysExisted = daysExisted;
			updateScore();
		}

	}


	/**
	 * Accesor for daysExisted field.
	 * 
	 * @return how long web page exists in days.
	 */
	public int getDaysExisted()
	{
		return this.daysExisted;
	}


	/**
	 * Update number of links that refer to the page if this number is greater than
	 * original and recalculate final score.
	 * 
	 * @param numberOfLinks
	 */
	public void setNumberOfLinks(int numberOfLinks)
	{
		if (numberOfLinks > this.numberOfLinks)
		{
			this.numberOfLinks = numberOfLinks;
			updateScore();
		}
	}


	/**
	 * Accessor for numberOfLinks field.
	 * 
	 * @return how many web pages refer to this pages
	 */
	public int getNumberOfLinks()
	{
		return this.numberOfLinks;
	}


	/**
	 * Update an amount of money payed by owner of the web page if it is greater
	 * than previous value and recalculate final score.
	 * 
	 * @param adMoney
	 */
	public void setAdMoney(int adMoney)
	{
		if (adMoney > this.adMoney)
		{
			this.adMoney = adMoney;
			updateScore();
		}
	}


	/**
	 * Accesor for adMoney field.
	 * 
	 * @return amount of money payed to promote this web page.
	 */
	public int getAdMoney()
	{
		return this.adMoney;
	}


	/**
	 * Convert list of WebPage objects into string representation for printint in
	 * tables.
	 * 
	 * @param urls
	 *            array of web pages
	 * @return 2d array of Strings
	 */
	public static String[][] prepareTableValues(WebPage[] urls)
	{
		String[][] webPages = new String[urls.length][5];
		for (int i = 0; i < urls.length; i++)
		{
			webPages[i][0] = "" + (i + 1);
			webPages[i][1] = "" + urls[i].getIndex();
			String color = urls[i].color == Color.BLACK ? "Black" : "Red";
			webPages[i][2] = "" + color;
			webPages[i][3] = "" + urls[i].getScore();
			webPages[i][4] = urls[i].getLink();
		}
		return webPages;
	}


	public String[] getArrayRepresentation()
	{
		return new String[] { "" + this.getIndex(), "" + this.getScore(), this.getLink() };
	}


	@Override
	public String toString()
	{
		return "score = " + getScore() + ",\n" + getLink();
		// return "Web page: " + getLink() + " : \t" + "score = " + getScore() + ", word
		// frequency = " + this.wordFrequency
		// + ", days = " + this.getDaysExisted() + ", number of links = " +
		// this.getNumberOfLinks()
		// + ", adMoney = " + this.getAdMoney() + ", current index = " + this.getIndex()
		// + ".";
	}
}
