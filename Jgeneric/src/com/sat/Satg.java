package com.sat;


public class Satg <E> {
    private E t;
    public void add (E t) {
    	this.t=t;
    }
   public E get()
   {
	   return t;
   }
    
    
	public static void main(String[] args) {
		
        Satg <String> a = new Satg <String> ();
        a.add("sathish");
        System.out.print(a.get());
        
        Satg <Integer> a1 = new Satg <Integer> ();
        a1.add(new Integer(10));
        System.out.print(a1.get());
        
	}

}
