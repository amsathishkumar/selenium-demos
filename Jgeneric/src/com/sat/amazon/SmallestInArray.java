package com.sat.amazon;

public class SmallestInArray {

	public static void main(String[] args) {
		int a[] = {3,1,2};

		int small=a[0];
		for(int i=1;i<a.length;i++){
			if (a[i]<small){
				small=a[i];
			}
		}
	   System.out.println(small);



	}

}
