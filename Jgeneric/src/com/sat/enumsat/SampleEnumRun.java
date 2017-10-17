package com.sat.enumsat;


public class SampleEnumRun {
	enum Season { WINTER, SPRING, SUMMER, FALL}
	public static void main(String args[]){

	System.out.println(SatsampleEnum.MAT);
	for (SatsampleEnum senum : SatsampleEnum.values())
	{
		System.out.println(senum );
		System.out.println(senum.get() );

	}
	 System.out.println(Season.WINTER);

		for(SatCurrency coin: SatCurrency.values()){
			   System.out.println("coin: " + coin);
			   System.out.println(coin.getValue());
			}

	}



}
