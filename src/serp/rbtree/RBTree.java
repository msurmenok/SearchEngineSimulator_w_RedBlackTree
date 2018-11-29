package serp.rbtree;

import java.util.ArrayList;

import serp.WebPage;

/**
 * Implementation of Red Black Tree data structure
 * 
 * @author msurmenok
 *
 */
public class RBTree
{
	public TreeNode root;
	private int size;
	public static final TreeNode nullNode;
	static
	{
		nullNode = new TreeNode(new WebPage("", 0, 0, 0, 0, -1));
		nullNode.left = nullNode;
		nullNode.right = nullNode;
		nullNode.p = nullNode;
	}


	public RBTree()
	{
		this.root = RBTree.nullNode;
		this.size = 0;
	}


	/**
	 * Remove all elements from BST
	 */
	public void clear()
	{
		this.root = RBTree.nullNode;
		this.size = 0;
	}


	/**
	 * Find web page with specific rank. Where rank = 1 has the biggest score
	 * 
	 * @param rank
	 *            page rank to find
	 * @return WebPage with specific rank
	 */
	public WebPage getDataByRank(int rank)
	{
		WebPage[] orderedPages = this.inOrderTreeWalk();
		int index = this.size - rank;

		if (index > 0 && rank > 0)
		{
			return orderedPages[this.size - rank];
		}
		return orderedPages[0];
	}


	/**
	 * Pivots around the link from x to y. Makes y the new root of the subtree with
	 * x as y's left child and y's left child as x's right child
	 * 
	 * @param T
	 *            red black tree
	 * @param x
	 *            root of the subtree
	 */
	private static void leftRotate(RBTree T, TreeNode x)
	{
		TreeNode y = x.right;
		x.right = y.left;
		if (y.left != RBTree.nullNode)
		{
			y.left.p = x;
		}
		y.p = x.p;
		if (x.p == RBTree.nullNode)
		{
			T.root = y;
		}
		else if (x == x.p.left)
		{
			x.p.left = y;
		}
		else
		{
			x.p.right = y;
		}
		y.left = x;
		x.p = y;
	}


	/**
	 * Pivots around the link from x to y. Makes y the new root of the subtree with
	 * x as y's right child and y's right child as x's left child
	 * 
	 * @param T
	 *            red black tree
	 * @param x
	 *            root of the subtree
	 */
	private static void rightRotate(RBTree T, TreeNode x)
	{
		TreeNode y = x.left;
		x.left = y.right;
		if (y.right != RBTree.nullNode)
		{
			y.right.p = x;
		}
		y.p = x.p;
		if (x.p == RBTree.nullNode)
		{
			T.root = y;
		}
		else if (x == x.p.right)
		{
			x.p.right = y;
		}
		else
		{
			x.p.left = y;
		}
		y.right = x;
		x.p = y;
	}


	/**
	 * Insert new node into tree T
	 * 
	 * @param T
	 *            reference to Red Black Tree
	 * @param z
	 *            data to be inserted
	 */
	public static void rbInsert(RBTree T, WebPage page)
	{
		TreeNode z = new TreeNode(page); // Create a node for our webpage
		TreeNode y = RBTree.nullNode;
		TreeNode x = T.root;

		while (x != RBTree.nullNode)
		{
			y = x;
			if (z.key < x.key)
			{
				x = x.left;
			}
			else
			{
				x = x.right;
			}
		}
		z.p = y;
		if (y == RBTree.nullNode)
		{
			T.root = z;
		}
		else if (z.key < y.key)
		{
			y.left = z;
		}
		else
		{
			y.right = z;
		}
		z.left = RBTree.nullNode;
		z.right = RBTree.nullNode;
		z.color = Color.RED;
		T.size++;
		rbInsertFixup(T, z);
	}


	/**
	 * Fixes violations of Red-Black Tree properties
	 * 
	 * @param T
	 *            red black tree
	 * @param z
	 *            node that was inserted, initially colored Red
	 */
	private static void rbInsertFixup(RBTree T, TreeNode z)
	{
		TreeNode y;
		while (z.p.color == Color.RED)
		{
			// TODO: Remove
			if (z.p == RBTree.nullNode)
			{
				System.out.println("ERROR!!!!!!!!!!1111   Should not get null node here");
			}
			// Up to here

			// z.p is not nullNode here
			// z.p.p can be anything, even a nullNode

			if (z.p == z.p.p.left)
			{

				y = z.p.p.right;
				if (y.color == Color.RED)
				{
					z.p.color = Color.BLACK;
					y.color = Color.BLACK;
					z.p.p.color = Color.RED;
					z = z.p.p;
				}
				else
				{
					if (z == z.p.right)
					{
						z = z.p;
						leftRotate(T, z);
					}
					z.p.color = Color.BLACK;
					// System.out.println(z.key + " ," + z.p.key);
					// if (z.p.p != RBTree.nullNode && z.p.p != null)
					// {
					z.p.p.color = Color.RED;
					rightRotate(T, z.p.p);
					// }

					if (z.p.p == RBTree.nullNode)
					{
						System.out.println("z.p.p is nullNode and we are reddening it O_o");
						System.out.println(z.p.p.color);
					}
				}
			}
			else
			{
				y = z.p.p.left;
				if (y.color == Color.RED)
				{
					z.p.color = Color.BLACK;
					y.color = Color.BLACK;
					z.p.p.color = Color.RED;
					z = z.p.p;
				}
				else
				{
					if (z == z.p.left)
					{
						z = z.p;
						rightRotate(T, z);
					}
					z.p.color = Color.BLACK;
					// System.out.println(z.key + " ," + z.p.key);
					// if (z.p.p != RBTree.nullNode && z.p.p != null)
					// {
					z.p.p.color = Color.RED;

					leftRotate(T, z.p.p);
					// }
					if (z.p.p == RBTree.nullNode)
					{
						System.out.println("z.p.p is nullNode and we are reddening it O_o");
					}
				}
			}
		}
		T.root.color = Color.BLACK;
	}


	/**
	 * Walk to the left-child, root node, right child such that it finds elements of
	 * BST in ascending order.
	 * 
	 * @return elements of BST in ascending order
	 */
	public WebPage[] inOrderTreeWalk()
	{
		ArrayList<WebPage> pages = new ArrayList<>();
		WebPage[] sortedPages = new WebPage[this.size];
		inOrderTreeWalk(this.root, pages);
		for (int i = 0; i < pages.size(); i++)
		{
			sortedPages[i] = pages.get(i);
		}
		return sortedPages;
	}


	private void inOrderTreeWalk(TreeNode x, ArrayList<WebPage> pages)
	{
		if (x != RBTree.nullNode)
		{
			inOrderTreeWalk(x.left, pages);
			x.data.color = x.color;
			pages.add(x.data);
			inOrderTreeWalk(x.right, pages);
		}
	}


	/**
	 * Find a successor, node with minimum value that is greater than key of x node
	 * 
	 * @param x
	 *            node for which we need to find a successor
	 * @return successor node
	 */
	private TreeNode treeSuccessor(TreeNode x)
	{
		if (x.right != null)
		{
			return treeMinumum(x);
		}
		TreeNode y = x.p;
		while (y != null && x == y.right)
		{
			x = y;
			y = y.p;
		}
		return y;
	}


	/**
	 * Finds an element in a binary search tree whose key is a minimum by in the
	 * subtree rooted at a node x. We assume that x != null
	 * 
	 * @param x
	 *            root of the subtree
	 * @return the node with minimum key
	 */
	public TreeNode treeMinumum(TreeNode x)
	{
		while (x.left != null && x.left.key != 0)
		{
			x = x.left;
		}
		return x;
	}


	/**
	 * Replaces one subtree as a child of its parent with another subtree
	 * 
	 * @param T
	 *            binary search tree that will be mutated
	 * @param u
	 *            the root of the subtree that will be replaced
	 * @param v
	 *            the root of the subtree that will replace u
	 */
	public static void rbTransplant(RBTree T, TreeNode u, TreeNode v)
	{
		if (u.p == RBTree.nullNode)
		{
			T.root = v;
		}
		else if (u == u.p.left)
		{
			u.p.left = v;
		}
		else
		{
			u.p.right = v;
		}

		v.p = u.p;

	}


	/**
	 * Delete node from RBTree
	 * 
	 * @param T
	 *            tree from which node will be deleted
	 * @param z
	 *            node to delete
	 */
	public static void rbDelete(RBTree T, TreeNode z)
	{
		if (z == null)
		{
			return;
		}
		T.size--;
		TreeNode y = z;
		Color yOriginalColor = y.color;
		TreeNode x;
		if (z.left == RBTree.nullNode)
		{
			x = z.right;
			rbTransplant(T, z, z.right);
		}
		else if (z.right == RBTree.nullNode)
		{
			x = z.left;
			rbTransplant(T, z, z.left);
		}
		else
		{
			y = T.treeMinumum(z.right);
			yOriginalColor = y.color;
			x = y.right;
			if (y.p == z)
			{
				x.p = y;
			}
			else
			{
				rbTransplant(T, y, y.right);
				y.right = z.right;
				y.right.p = y;
			}
			rbTransplant(T, z, y);
			y.left = z.left;
			y.left.p = y;
			y.color = z.color;
		}
		if (yOriginalColor == Color.BLACK)
		{
			rbDeleteFixup(T, x);
		}
	}


	public static void rbDeleteFixup(RBTree T, TreeNode x)
	{
		while (x != T.root && x.color == Color.BLACK)
		{
			if (x == x.p.left)
			{
				TreeNode w = x.p.right;
				if (w.color == Color.RED)
				{
					w.color = Color.BLACK;
					x.p.color = Color.RED;
					leftRotate(T, x.p);
					w = x.p.right;
				}
				if (w.left.color == Color.BLACK && w.right.color == Color.BLACK)
				{
					w.color = Color.RED;
					x = x.p;
				}
				else
				{
					if (w.right.color == Color.BLACK)
					{

						w.left.color = Color.BLACK;
						w.color = Color.RED;
						rightRotate(T, w);
						w = x.p.right;
					}

					w.color = x.p.color;
					x.p.color = Color.BLACK;
					w.right.color = Color.BLACK;
					leftRotate(T, x.p);
					x = T.root;
				}
			}
			else
			{
				TreeNode w = x.p.left;
				if (w.color == Color.RED)
				{
					w.color = Color.BLACK;
					x.p.color = Color.RED;
					rightRotate(T, x.p);
					w = x.p.left;
				}
				if (w.left.color == Color.BLACK && w.right.color == Color.BLACK)
				{
					w.color = Color.RED;
					x = x.p;
				}
				else
				{
					if (w.left.color == Color.BLACK)
					{
						w.right.color = Color.BLACK;
						w.color = Color.RED;
						leftRotate(T, w);
						w = x.p.left;
					}
					w.color = x.p.color;
					x.p.color = Color.BLACK;
					w.left.color = Color.BLACK;
					rightRotate(T, x.p);
					x = T.root;
				}
			}
		}
		x.color = Color.BLACK;
	}


	/**
	 * Finds a node with specific webpage score
	 * 
	 * @param k
	 *            score of the web page
	 * @return the first node with specified score, null if there is no such element
	 */
	public TreeNode treeSearch(int k)
	{
		System.out.println(this.treeSearch(this.root, k));
		return this.treeSearch(this.root, k);
	}


	/**
	 * Subroutine to find a node with specific webpage score
	 * 
	 * @param x
	 *            the root of the subtree where we are looking for a node
	 * @param k
	 *            score of the web page
	 * @return the first node with specified score, null if there is no such element
	 */
	private TreeNode treeSearch(TreeNode x, int k)
	{
		if (x == RBTree.nullNode || k == x.key)
		{
			return x;
		}
		if (k < x.key)
		{
			return treeSearch(x.left, k);
		}
		else
		{
			return treeSearch(x.right, k);
		}
	}
}
