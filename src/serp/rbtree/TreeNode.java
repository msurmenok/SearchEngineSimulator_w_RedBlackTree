package serp.rbtree;

import serp.WebPage;

/**
 * Simple element of BST. Instance variable key is web page score.
 * 
 * @author msurmenok
 *
 */
public class TreeNode
{
	public int key;
	public WebPage data;
	public TreeNode left;
	public TreeNode right;
	public TreeNode p;
	public Color color;


	public TreeNode(WebPage data)
	{
		this(data, null, null);
	}


	public TreeNode(WebPage data, TreeNode left, TreeNode right)
	{
		this.key = data.getScore();
		this.data = data;
		this.color = Color.BLACK;
		this.left = left;
		this.right = right;
	}
}
