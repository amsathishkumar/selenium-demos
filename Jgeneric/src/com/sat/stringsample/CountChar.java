package com.sat.stringsample;

import java.util.HashMap;
import java.util.Map;

import javax.management.MXBean;

public class CountChar {

	public static void main(String[] args) {
		String name = "sathishkumar";
		int maxcount = -1;
		String maxcar = null;
		
		Map <String,Integer> set = new HashMap<String,Integer>();
		for(int i=0;i<name.length();i++){
		  String keyStr= Character.toString(name.charAt(i));	
		  if (! set.containsKey(keyStr)){	
		   set.put(keyStr, new Integer(0));
		  }
		  int countincr= set.get(keyStr) + 1;
		  set.put(keyStr,countincr);
		  
		  if (maxcount<=countincr)
		  {
			  maxcar = keyStr;
			  maxcount = countincr;
		  }
			  
		      
		}
		System.out.print(set);
	    System.out.println(maxcar +' ' + maxcount);
		
	   
		
		

	}

}
