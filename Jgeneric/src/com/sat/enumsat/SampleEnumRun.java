package com.sat.enumsat;


public class SampleEnumRun {
	enum Season { WINTER, SPRING, SUMMER, FALL}
	public static void main(String args[]){

	System.out.println(SatsampleEnum.MAT);
	for (SatsampleEnum senum : SatsampleEnum.values())
	{
		System.out.println(senum );
	}
	 System.out.println(Season.WINTER);
	 
	}
}
