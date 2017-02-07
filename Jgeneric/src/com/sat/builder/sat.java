package com.sat.builder;

public class sat {
	public static void main(String a[]){
		user ub = new user.userBuilder("1").withfname("ast").withlname("kumar").build();
		System.out.println(ub.getName());
	}
     
}
