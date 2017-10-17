package com.sat.single;

public class Mysingleton {
	private static Mysingleton mst;
	private int sat=0;
	private Mysingleton ()
	{
		
	}
	public static Mysingleton getInstance()
	{
		if (mst == null)
		  mst = new Mysingleton();
		return mst;
	}
	 public void showMessage(){
	      System.out.println("Hello World!");
	      sat++;
	      System.out.println(sat);
	   }
}


