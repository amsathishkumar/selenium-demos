package com.sat.stringsample;

public class ConvChar2String {
	public static void main(String[] args) {
//		1. String stringValueOf = String.valueOf('c'); // most efficient
		String stringValueOf = String.valueOf('c');
		System.out.println(stringValueOf);
//
//		2. String stringValueOfCharArray = String.valueOf(new char[]{x});
//
//		3. String characterToString = Character.toString('c');
		String characterToString = Character.toString('c');
		System.out.println(characterToString);
//
//		4. String characterObjectToString = new Character('c').toString();
		String characterObjectToString = new Character('c').toString();
		System.out.println(characterObjectToString);
//
//		   // Although this method seems very simple, 
//		   // this is less efficient because the concatenation
//		   // expands to new StringBuilder().append(x).append("").toString();
//		5. String concatBlankString = 'c' + "";
//
//		6. String fromCharArray = new String(new char[]{x});
		
	}

}
