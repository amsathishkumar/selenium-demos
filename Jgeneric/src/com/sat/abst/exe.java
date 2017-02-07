package com.sat.abst;

public class exe {
	
	public static void main(String a[]){
		AbstImpl absimpl = new AbstImpl();
		absimpl.setfName("sat");
		absimpl.setLName("last");
		System.out.println(absimpl.getName());
		
		AbstImpl1 absimpl1 = new AbstImpl1();
		absimpl1.setfName("sat");
		absimpl1.setLName("last");
		System.out.println(absimpl1.getName());
	}


}
