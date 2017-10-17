package com.sat.max;

import java.util.ArrayList;

public class AddvalueRange {
  public static void main(String args[]){
	  ArrayList <MinMax> fminmax = new ArrayList <MinMax>() ;
	  fminmax.add(new MinMax(0,0));
    ArrayList <MinMax> minmax = new ArrayList <MinMax>() ;
    minmax.add(new MinMax(200,500));
    minmax.add(new MinMax(50,10000));
    minmax.add(new MinMax(300,500));
    minmax.add(new MinMax(100,500));
    minmax.add(new MinMax(10,15));
    minmax.add(new MinMax(5,10));
    minmax.add(new MinMax(1,50));
    minmax.add(new MinMax(3,4));
//    minmax.add(new MinMax(50,10000));
  //  minmax.add(new MinMax(1,50));


    for (MinMax vl : minmax){
        System.out.println("Min"+ vl.min + "Max"+ vl.max);
    }

    System.out.println("Processing\n");

    for(int i=0;i<minmax.size();i++)
    {
      for(int j= i+1;j<minmax.size();j++)
      {
        if((minmax.get(i).min<=minmax.get(j).min) && (minmax.get(i).max>=minmax.get(j).max) )
    	  {
        	System.out.println("ku");
        	  minmax.get(j).min=minmax.get(i).min;
        	  minmax.get(j).max=minmax.get(i).max;
        	  minmax.remove(i) ;
    	  }else if((minmax.get(j).min<=minmax.get(i).min) && (minmax.get(j).max>=minmax.get(i).max) )
    	  {
    		  System.out.println("sat");
        	  minmax.get(i).min=minmax.get(j).min;
        	  minmax.get(i).max=minmax.get(j).max;
        	  minmax.remove(j) ;
    	  }
      }

      }

    System.out.println("After refactor");
    for(MinMax fsvalue: minmax)
    {
    	System.out.println("Min:"+ fsvalue.min +" Max:" + fsvalue.max);
    }

  }
}
