package com.sat.amazon;

public class GCD {
	public static void main(String args[]){
		int arr[] = {2, 4, 6, 8, 16};
	    int n = arr.length;
	    System.out.println(findGCD(arr, n));
	}
	 static int findGCD(int arr[], int n){

		 int result= arr[0];
		 for (int i =1;i< n;i++){
			 result = gcd(arr[i],result);
		 }
		return result;

	 }

	static int gcd(int a,int b){

	 if (a==0)
		return b;
	return gcd(b%a,a);


	}
}
