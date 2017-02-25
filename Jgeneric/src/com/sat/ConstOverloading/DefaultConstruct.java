package com.sat.ConstOverloading;
//Another important point to note while overloading a constructor is: When we don’t define any constructor, the compiler creates the default constructor(also known as no-arg constructor) by default during compilation however if we have defined a parameterized constructor and didn’t define a no-arg constructor then while calling no-arg constructor the program would fail as in this case compiler doesn’t create a no-arg constructor.

public class DefaultConstruct {
	
	DefaultConstruct(String a){
       System.out.println("Name"+a);
	}
}

class DefaultConstructRun{
	DefaultConstruct dd  = new DefaultConstruct();
	
}
