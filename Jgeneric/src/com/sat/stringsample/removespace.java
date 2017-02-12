package com.sat.stringsample;

public class removespace {
	public static void main(String args[])
	{
		/*
		        \w = Anything that is a word character

				\W = Anything that isn't a word character (including punctuation etc)

				\s = Anything that is a space character (including space, tab characters etc)

				\S = Anything that isn't a space character (including both letters and numbers, as well as punctuation etc)
		*/		
		String name = "sathish kumar";		
		System.out.println(name.replaceAll("\\s", ""));
		
		for(int i=0;i<name.length();i++){
			if (name.charAt(i)!=' ')
				System.out.print(name.charAt(i));
				
		}
		
		char a[] = name.toCharArray();
		for(int i=0;i<a.length;i++)
			if(a[i]!=' ')
			System.out.print(a[i]);
		
	}

}
