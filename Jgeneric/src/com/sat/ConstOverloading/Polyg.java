package com.sat.ConstOverloading;

class Polymas {
	public void m1(){
	System.out.println("demo1");
	}
}
public class Polyg extends Polymas{
	public void m1(){
	System.out.println("demo2");
	System.out.println("sass");
	}

	public static void main(String arg[]){
		Polyg p = new Polyg();
		Polymas p2 = new Polyg();
		p2.m1();
		p.m1();
		Polymas p3 = new Polymas();
		p3.m1();
	}

}
