package com.example.cadpart;
// Java program to construct BST from given preorder traversal

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.vaadin.data.Item;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeTable;

import at.smartpart.beans.Main;
import at.smartpart.beans.MyTreeNode;

class PreOrderBST {

	TreeTable treeTable = new TreeTable("test");
	int x = 0;
	int y = 0;


	Map<Integer, Integer> sibs = new HashMap<Integer, Integer>();
	private Tree tree;
	private List<List<String>> bom = new ArrayList<List<String>>();
	private List<DefaultMutableTreeNode> dedupedList = new ArrayList<DefaultMutableTreeNode>();
	private List<TreeNode> nodeList = new ArrayList<TreeNode>();
	
	List<TreePath> pathAsList = new ArrayList<TreePath>();
	private List<DefaultMutableTreeNode> list;


	public PreOrderBST(JTree mdl) {

		treeTable.addContainerProperty("Dateiname", String.class, null);
		treeTable.addContainerProperty("Version", String.class, null);
		treeTable.addContainerProperty("Anzahl", Integer.class, null);
		
		


	}

	public PreOrderBST() {

		// tree = createStaticTree();
	}
	
	public void createJTree(JTree jTree) {
		
		TreeModel treeModel = jTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
		tree = new Tree(root.getUserObject().toString());
		traverse(root);		
	
	}

	
	public void createBom() {
		
		System.out.print("generating m-level-bom");
		
		for(TreeNode node : dedupedList) {
			
			
			int count = countOcc(nodeList , node);
			
			System.out.println("Item : " + ((DefaultMutableTreeNode)node).getUserObject().toString() + "Anzahl : " + count);
			
		}
	}
	
	TreeNode newChild;
	
	public  void traverse(DefaultMutableTreeNode node) {
		String tempGroup = "";
		if(!node.isLeaf()) {
			
			String group = (String) (node.getUserObject().toString());
			//System.out.println("group" + group);
			//tree.addItem(group);
			tree.addItem(group);
			
			tempGroup = group; 
			
			Enumeration e = node.children();
			while (e.hasMoreElements()) {
				
			newChild = (TreeNode) e.nextElement();
			String child = ((DefaultMutableTreeNode) newChild).getUserObject().toString();
			tree.setParent(child, group);
			
			traverse((DefaultMutableTreeNode) newChild);
			}
			
		}
		else
		{
			String child = ((DefaultMutableTreeNode) newChild).getUserObject().toString();
			//System.out.println("child" + child);
			tree.addItem(child);
			tree.setParent(child , tempGroup);
			tree.setChildrenAllowed(child, false);
		}
		
		
		
	}
	
	
	public int countOcc(List<TreeNode> nodes, TreeNode node) {
		int count = 0;
		for(TreeNode item : nodes) {
			
			String sItem = ((DefaultMutableTreeNode) item).getUserObject().toString();
			String sNode = ((DefaultMutableTreeNode) node).getUserObject().toString();
			
			System.out.println(sItem);
			System.out.println(sNode);
			
			if(sItem.equals(sNode)){
				count++;
			}
		}return count;
	}



	public TreePath[] getPaths(JTree tree, boolean expanded) {
		TreeNode node = (TreeNode) tree.getModel().getRoot();
		List<TreePath> pathList = new ArrayList<TreePath>();
		getPaths(tree, new TreePath(node), expanded, pathList);

		pathAsList = pathList;

		return (TreePath[]) pathList.toArray(new TreePath[pathList.size()]);
	}

	public void getPaths(JTree tree, TreePath parent, boolean expanded, List<TreePath> list) {
		if (expanded && !tree.isVisible(parent)) {
			return;
		}
		list.add(parent);
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		nodeList.add(node);
		if (node.getChildCount() > 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.nextElement();
				////////////////////////////////////////////////////////////
				//
				//String userObj = (String) n.getUserObject();
				//System.out.println("obj : " + userObj);
				if (!dedupedList.contains(n)) {
					dedupedList.add(n);
				}
				TreeNode x = (TreeNode)n;
				nodeList.add(x);
				////////////////////////////////////////////////////////////
				TreePath path = parent.pathByAddingChild(n);
				getPaths(tree, path, expanded, list);
			}
		}
	}

	

	public TreeTable returnTable() {

		return treeTable;
	}

	public Tree returnTree() {

		return tree;
	}
}
