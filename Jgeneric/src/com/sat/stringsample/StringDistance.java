package com.sat.stringsample;

public class StringDistance {
	public static void main(String args[])
	{
		String str1 = "geeksforgeeks" ;  
		String str2 = "geeksforgeek" ;
		int k = 1;
		
		if(str1.length() > str2.length() - k){
			System.out.print('y');
		    System.out.println(str1.replace(str2, ""));
		}
		else
			System.out.println('n');
		
		  int i = Integer.parseInt("00101011",2);
		  System.out.println( Integer.toBinaryString(i));
		  i >>= 2;
		  System.out.println( Integer.toBinaryString(i));
		
	}

}
