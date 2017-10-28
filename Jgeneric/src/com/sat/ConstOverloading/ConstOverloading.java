package com.sat.ConstOverloading;

public class ConstOverloading
{
   private int rollNum;
   ConstOverloading()
   {
      rollNum =100;
   }
   ConstOverloading(int rnum)
   {
      rollNum = rollNum+ rnum;
      //has to be called first as first line
      this();
   }
   public int getRollNum() {
	  return rollNum;
   }
   public void setRollNum(int rollNum) {
	  this.rollNum = rollNum;
   }
}
class TestDemo{
   public static void main(String args[])
   {
       ConstOverloading obj = new ConstOverloading(12);
       System.out.println(obj.getRollNum());
    }
}

//Exception in thread "main" java.lang.Error: Unresolved compilation
//problem:Constructor call must be the first statement in a constructor line 13