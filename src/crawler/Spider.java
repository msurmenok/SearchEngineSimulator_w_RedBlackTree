package crawler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider
{
    private static final int MAX_PAGES_TO_SEARCH = 150;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();


    /**
     * Our main launching point for the Spider's functionality. Internally it
     * creates spider legs that make an HTTP request and parse the response (the web
     * page).
     * 
     * @param url
     *            - The starting point of the spider
     * @param searchWord
     *            - The word or string that you are searching for
     */
    public Set<String> search(String url, String searchWord)
    {
	int indexOfSign = url.indexOf('#');
	if (indexOfSign != -1)
	{
	    url = url.substring(0, indexOfSign);
	}
	Set<String> relevantLinks = new HashSet<String>();
	while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
	{
	    String currentUrl;
	    SpiderLeg leg = new SpiderLeg();
	    if (this.pagesToVisit.isEmpty())
	    {
		currentUrl = url;
		this.pagesVisited.add(url);
	    } else
	    {
		currentUrl = this.nextUrl();
	    }
	    
	    indexOfSign = currentUrl.indexOf('#');
	    if (indexOfSign != -1)
	    {
		currentUrl = currentUrl.substring(0, indexOfSign);
	    }
	    
	    leg.crawl(currentUrl); // Lots of stuff happening here. Look at the crawl method in
				   // SpiderLeg
	    boolean success = leg.searchForWord(searchWord);
	    if (success)
	    {
		// System.out.println(String.format("**Success** Word %s found at %s",
		// searchWord, currentUrl));
		// break;
		if (relevantLinks.size() >= 30)
		{
		    break;
		}
		relevantLinks.add(currentUrl);
	    }
	    this.pagesToVisit.addAll(leg.getLinks());
	}
	// System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web
	// page(s)");
	return relevantLinks;
    }


    /**
     * Returns the next URL to visit (in the order that they were found). We also do
     * a check to make sure this method doesn't return a URL that has already been
     * visited.
     * 
     * @return
     */
    private String nextUrl()
    {
	String nextUrl;
	do
	{
	    nextUrl = this.pagesToVisit.remove(0);
	} while (this.pagesVisited.contains(nextUrl));
	this.pagesVisited.add(nextUrl);
	return nextUrl;
    }
}