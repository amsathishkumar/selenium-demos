package com.sat.single;

public class CallSingle {
	public static void main (String args[]){
		Mysingleton mst1= Mysingleton.getInstance();
		mst1.showMessage();
		Mysingleton mst2 = Mysingleton.getInstance();
		mst2.showMessage();
		
	}

}
