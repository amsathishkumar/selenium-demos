package com.sat.enumsat;

public enum SatsampleEnum {
SAT(1), MAT(2);
private int value;
private SatsampleEnum(int value){
 this.value =value;
}

public int get()
{
	return value;
}
}


