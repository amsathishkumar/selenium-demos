package com.sat.ConstOverloading;

//overriding or runtime ploymorphosim
class Polymas {
	public void m1(){
	System.out.println("demo1");
	}
}
public class Overriding extends Polymas{
	public void m1(){
	System.out.println("demo2");
	System.out.println("sass");
	}

	public static void main(String arg[]){
		Overriding p = new Overriding();
		Polymas p2 = new Overriding();
		p2.m1();
		p.m1();
		Polymas p3 = new Polymas();
		p3.m1();
	}

}
