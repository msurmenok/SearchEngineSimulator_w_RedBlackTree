package view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import serp.PageRanker;
import serp.WebPage;
import serp.rbtree.RBTree;
import serp.rbtree.TreeNode;

/**
 * GUI representation to the second part of Programming Assignment. Use Binary
 * Search Tree to manipulate web links
 * 
 * @author msurmenok
 *
 */
public class DynamicPageRankerView
{
	JTable topLinksTable;
	JLabel title;
	WebPage[] urls;


	public DynamicPageRankerView(WebPage[] urls, String defaultKeyword, RBTree T)
	{
		this.urls = urls;
		RBTree redBlackTree = T;
		String[] columnNames = { "Page Rank", "Original Index", "Color", "Score", "Link" };

		////////// SEARCHPANEL
		// Panel to search with custom keyword
		// Has two elements: textbox for keyword and button to initiate new search.
		JPanel searchPanel = new JPanel();

		searchPanel.add(new JLabel("Run a new search:"));
		JTextField keywordField = new JTextField(15);
		keywordField.setText(defaultKeyword);
		JButton searchButton = new JButton("Search");
		// Handle the new search
		searchButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

				String newKeyword = keywordField.getText();
				WebPage[] urls = PageRanker.getLinks(newKeyword);
				redBlackTree.clear();
				for (WebPage url : urls)
				{
					RBTree.rbInsert(redBlackTree, url);
				}
				WebPage[] sortedPages = redBlackTree.inOrderTreeWalk();
				sortedPages = PageRanker.reverse(sortedPages);
				// Rerdraw the table
				String[][] urlData = WebPage.prepareTableValues(sortedPages);
				DefaultTableModel model = new DefaultTableModel(urlData, columnNames);
				topLinksTable.setModel(model);
				topLinksTable.getColumnModel().getColumn(4).setMinWidth(300);

			}
		});
		searchPanel.add(keywordField);
		searchPanel.add(searchButton);

		////////// SHOWLINKSPANEL
		// Panel to show 30 sorted urls as a table
		JPanel showLinksPanel = new JPanel();
		showLinksPanel.setLayout(new BorderLayout());
		title = new JLabel("Sorted urls for keyword \"" + defaultKeyword + "\"");
		showLinksPanel.add(title, BorderLayout.CENTER);

		// String representation of sorted urls
		String[][] topThirtyToPrint = WebPage.prepareTableValues(urls);

		// Create column names

		// Populate the table with urls' values and column names
		this.topLinksTable = new JTable(topThirtyToPrint, columnNames);

		// Create scroll bar for the table
		JScrollPane tableScroller = new JScrollPane(this.topLinksTable);
		topLinksTable.getColumnModel().getColumn(4).setMinWidth(300);
		tableScroller.setPreferredSize(new Dimension(600, 600));
		showLinksPanel.add(tableScroller, BorderLayout.SOUTH);

		////////// LEFTPANEL
		// Panel for insert, search and delete functions
		JPanel leftPanel = new JPanel();

		leftPanel.add(new JLabel("Search by Page Rank"));

		////////// SEARCHURLPANEL
		JPanel searchUrlPanel = new JPanel(new FormLayout());
		searchUrlPanel.add(new JLabel("Page Rank: "));
		JTextField pageRankToSearchField = new JTextField(15);
		JButton searchUrlButton = new JButton("Search url");
		JTextArea foundUrlField = new JTextArea(3, 16);

		//////// LISTENER TO SEARCH URL BY ITS RANK
		searchUrlButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int rank = 0;
				try
				{
					rank = Integer.parseInt(pageRankToSearchField.getText());
				}
				catch (Exception exception)
				{
					System.out.println(exception.getMessage());
				}
				WebPage page = redBlackTree.getDataByRank(rank);
				// Write information about page to foundUrlField
				foundUrlField.setText(page.toString());
			}
		});

		searchUrlPanel.add(pageRankToSearchField);
		searchUrlPanel.add(searchUrlButton);
		searchUrlPanel.add(foundUrlField);

		leftPanel.add(searchUrlPanel);
		leftPanel.add(new JLabel("Delete by Score"));

		////////// DELETEURLPANEL
		// Panel to remove a web page by its score
		JPanel deleteUrlPanel = new JPanel(new FormLayout());
		JTextField scoreToDeleteField = new JTextField(15);
		JButton deleteUrlButton = new JButton("Delete url");

		//////// LISTENER TO DELETE ELEMENT
		deleteUrlButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int score = 0;
				try
				{
					score = Integer.parseInt(scoreToDeleteField.getText()); // Get user input
				}
				catch (Exception exception)
				{
					System.out.println(exception.getMessage());
				}

				// Find Node with specified score
				TreeNode nodeToDelete = redBlackTree.treeSearch(score);

				// Delete node if found one
				if (nodeToDelete != null)
				{
					RBTree.rbDelete(redBlackTree, nodeToDelete);
					System.out.println("=== After deletion");
					PageRanker.validateTree(redBlackTree);
					PageRanker.checkDepth(redBlackTree.root, 1, 1);
				}

				// Get sorted pages and reverse the order
				WebPage[] sortedPages = redBlackTree.inOrderTreeWalk();
				sortedPages = PageRanker.reverse(sortedPages);
				// Rerdraw the table
				String[][] urlData = WebPage.prepareTableValues(sortedPages);
				DefaultTableModel model = new DefaultTableModel(urlData, columnNames);
				topLinksTable.setModel(model);
				topLinksTable.getColumnModel().getColumn(4).setMinWidth(300);
			}
		});

		deleteUrlPanel.add(new JLabel("Score: "));
		deleteUrlPanel.add(scoreToDeleteField);
		deleteUrlPanel.add(deleteUrlButton);
		deleteUrlPanel.add(new JLabel(""));

		leftPanel.add(deleteUrlPanel);

		leftPanel.add(new JLabel("Insert a new URL"));

		////////// INSERTPAGEPANEL
		// Panel to add new web page to the list
		JPanel insertUrlPanel = new JPanel();
		insertUrlPanel.setLayout(new FormLayout());

		// Add url, word frequency, days, num of references, and money fields
		insertUrlPanel.add(new JLabel("Url"));
		JTextField urlField = new JTextField(15);
		insertUrlPanel.add(urlField);

		insertUrlPanel.add(new JLabel("Word Frequency"));
		JTextField frequencyField = new JTextField(15);
		insertUrlPanel.add(frequencyField);

		insertUrlPanel.add(new JLabel("Days existed"));
		JTextField daysField = new JTextField(15);
		insertUrlPanel.add(daysField);

		insertUrlPanel.add(new JLabel("# of references"));
		JTextField referencesField = new JTextField(15);
		insertUrlPanel.add(referencesField);

		insertUrlPanel.add(new JLabel("Money"));
		JTextField moneyField = new JTextField(15);
		insertUrlPanel.add(moneyField);
		// Button to create and insert new web page into BST
		JButton addUrlButton = new JButton("Add url");

		//////// LISTENER TO INSERT ELEMENT
		addUrlButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Read all values to create a new WebPage instance
				//
				String url = urlField.getText();
				int frequency = 0;
				int numberOfDays = 0;
				int numberOfLinks = 0;
				int adMoney = 0;
				try
				{
					// Parse textfields values
					frequency = Integer.parseInt(frequencyField.getText());
					numberOfDays = Integer.parseInt(daysField.getText());
					numberOfLinks = Integer.parseInt(referencesField.getText());
					adMoney = Integer.parseInt(moneyField.getText());
				}
				catch (Exception exception)
				{
					System.out.println(exception.getMessage());
				}
				WebPage page = new WebPage(url, frequency, numberOfDays, numberOfLinks, adMoney, -1);
				RBTree.rbInsert(redBlackTree, page);
				
				// Check RBTree structure
				System.out.println("=== After insertion");
				PageRanker.validateTree(redBlackTree);
				PageRanker.checkDepth(redBlackTree.root, 1, 1);
				
				// Get sorted pages and reverse the order
				WebPage[] sortedPages = redBlackTree.inOrderTreeWalk();
				sortedPages = PageRanker.reverse(sortedPages);
				// Rerdraw the table
				String[][] urlData = WebPage.prepareTableValues(sortedPages);
				DefaultTableModel model = new DefaultTableModel(urlData, columnNames);
				topLinksTable.setModel(model);
				topLinksTable.getColumnModel().getColumn(4).setMinWidth(300);
			}
		});

		insertUrlPanel.add(new JLabel(""));
		insertUrlPanel.add(addUrlButton);

		// insert inside left panel insertPagePanel.add(addUrl);
		leftPanel.add(insertUrlPanel);
		// leftPanel.add(addUrlButton);

		// Create boxLayout for left panel to arrange elements in stack
		BoxLayout boxLayout = new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
		leftPanel.setLayout(boxLayout);

		////////// FRAME
		// Create JFrame and add panel with table into it
		JFrame frame = new JFrame("Page Ranker using Red Black Tree");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(searchPanel, BorderLayout.NORTH);
		frame.add(tableScroller, BorderLayout.CENTER);
		frame.add(leftPanel, BorderLayout.WEST);

		frame.pack();
		frame.setVisible(true);

	}


	public void run()
	{
	}
}
