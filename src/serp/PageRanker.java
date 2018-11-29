package serp;

import java.util.ArrayList;

import crawler.Spider;
import serp.rbtree.Color;
import serp.rbtree.RBTree;
import serp.rbtree.TreeNode;
import view.DynamicPageRankerView;

/**
 * PageRanker initiate the program. Provides helper function to retrieve urls
 * from web crawler.
 * 
 * @author msurmenok
 *
 */
public class PageRanker
{

	/**
	 * Entry point for PageRanker application
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		System.out.println("STARTED");
		// Get links from web crawler for specified url and keyword.
		String searchWord = "business";
		//WebPage[] webPages = PageRanker.getLinks(searchWord);

		WebPage[] webPages = new WebPage[30];
		for(int i = 0; i < 30; i++) {
			webPages[i] = new WebPage("", 1 + i, 0, 0, 0, 0);
		}
		
		// Initial input
		System.out.println("INITIAL INPUT");
		for (int i = 0; i < webPages.length; i++)
		{
			System.out.format("%-5d %-5d %-80s %5d\n", i, webPages[i].getIndex(), webPages[i].getLink(),
					webPages[i].getScore());
		}

		// Create BST
		RBTree redBlackTree = new RBTree();
		for (WebPage page : webPages)
		{
			if(page.getScore() == 4) {
				System.out.println("Start debugging");
			}
			
			RBTree.rbInsert(redBlackTree, page);
			System.out.println("===== Tree");
			printTree(redBlackTree.root, "");
		}

		// Sort using in-order traverse
		WebPage[] sortedPages = redBlackTree.inOrderTreeWalk();
		sortedPages = PageRanker.reverse(sortedPages);

		// Print sorted elements
		System.out.println("\nSored using BST");
		for (int i = 0; i < sortedPages.length; i++)
		{
			System.out.format("%-5d %-5d %-80s %5d\n", i, sortedPages[i].getIndex(), sortedPages[i].getLink(),
					sortedPages[i].getScore());
		}

		if(redBlackTree.root.color != Color.BLACK) {
			throw new Exception("Root is not black");
		}
		validateTree(redBlackTree);
		checkDepth(redBlackTree.root, 1, 1);
		
		DynamicPageRankerView view = new DynamicPageRankerView(sortedPages, searchWord, redBlackTree);
	}

	public static void validateTree(RBTree tree) {
		WebPage[] sortedPages = tree.inOrderTreeWalk();
		for(int i = 1; i < sortedPages.length; i++) {
			if(sortedPages[i].getScore() < sortedPages[i-1].getScore()) {
				System.out.println("Ploho!");
			}
		}
	}
	
	public static void checkDepth(TreeNode node, int depth, int numBlack) {
		if(node.left == RBTree.nullNode && node.right == RBTree.nullNode) {
			System.out.println("Node " + node.key + " depth " + depth + " numBlack " + numBlack);
			return;
		}
		if(node.left != RBTree.nullNode) {
			checkDepth(node.left, depth + 1, node.left.color == Color.BLACK ? numBlack + 1 : numBlack);
		}
		if(node.right != RBTree.nullNode) {
			checkDepth(node.right, depth + 1, node.right.color == Color.BLACK ? numBlack + 1 : numBlack);
		}
	}
	
	public static void printTree(TreeNode node, String indent) {
		if(node.left != RBTree.nullNode) {
			printTree(node.left, indent + "+");
		}
		System.out.println(indent + node.key + " " + node.color);
		if(node.right != RBTree.nullNode) {
			printTree(node.right, indent + "+");
		}
	}

	/**
	 * Call web crawler and generate 30 links for a particular keyword
	 * 
	 * @param searchWord
	 *            keyword to search in web pages
	 * @return list of 30 links with generated scores
	 */
	public static WebPage[] getLinks(String searchWord)
	{
		// Call crawler
		Spider spider = new Spider();
		String url = "https://www.zyxware.com/articles/4344/list-of-fortune-500-companies-and-their-websites";

		ArrayList<String> links = new ArrayList<>(spider.search(url, searchWord));

		// Create list of web pages
		WebPage[] webPages = new WebPage[links.size()];
		for (int i = 0; i < links.size(); i++)
		{
			int wordFrequency = generateRandom(100);
			int daysExisted = generateRandom(100);
			int numberOfLinks = generateRandom(100);
			int moneyPayed = generateRandom(100);
			String link = links.get(i);
			if (link.length() > 40)
			{
				link = link.substring(0, 40);
			}
			WebPage webPage = new WebPage(link, wordFrequency, daysExisted, numberOfLinks, moneyPayed, i);
			webPages[i] = webPage;

		}
		return webPages;
	}


	/**
	 * Reverse indices in a list of webpages
	 * 
	 * @param webPages
	 *            list to be reversed
	 * @return reversed list of web pages
	 */
	public static WebPage[] reverse(WebPage[] webPages)
	{
		WebPage[] reversedPages = new WebPage[webPages.length];
		int j = webPages.length - 1;
		for (int i = 0; i < reversedPages.length; i++)
		{
			reversedPages[i] = webPages[j];
			j--;
		}
		return reversedPages;
	}


	/**
	 * Generate random number.
	 * 
	 * @param maxValue
	 *            the maximum random number.
	 * @return random number from 0 to max value
	 */
	private static int generateRandom(int maxValue)
	{
		return 1 + (int) (Math.random() * (maxValue + 1));
	}
}
