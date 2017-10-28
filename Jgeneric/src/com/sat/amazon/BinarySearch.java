package com.sat.amazon;

class node {
	node left;
	node right;
	Integer data;
	node(){
		left =null;
		right = null;
        data = null;
	}
}

class binarytree{
	node head;
	public void nodecreate (Integer value){
		node bst = new node();
		bst.data = value;
		if (head == null){
			head = bst;
		}else{
		  if (value < head.data){
			  System.out.println("left");
			  head.left = bst;
		  }else{
			  System.out.println("rigth");
			  head.right = bst;
		  }

		}
	}
	public void printtree(node phead)
	{
		if (phead != null)
		{
		   printtree(phead.left);
		   System.out.println(phead.data);
		   printtree(phead.right);

		}
		else
			return ;
	}


}

class BinarySearch {

	public static void main(String args[])
	{
		binarytree bt = new binarytree();
		bt.nodecreate(4);
		bt.nodecreate(5);
		bt.nodecreate(3);
		bt.printtree(bt.head);

	}

}
