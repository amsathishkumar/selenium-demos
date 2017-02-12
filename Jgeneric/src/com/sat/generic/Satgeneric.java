package com.sat.generic;


public class Satgeneric <E> {
    private E t;
    public void add (E t) {
    	this.t=t;
    }
   public E get()
   {
	   return t;
   }
    
    
	public static void main(String[] args) {
		
        Satgeneric <String> a = new Satgeneric <String> ();
        a.add("sathish");
        System.out.print(a.get());
        
        Satgeneric <Integer> a1 = new Satgeneric <Integer> ();
        a1.add(new Integer(10));
        System.out.print(a1.get());
        
	}

}
