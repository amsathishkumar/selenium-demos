package com.sat.multiinhert;

public class TestIntr implements Inte3{

	@Override
	public int a() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int b() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int d() {
		// TODO Auto-generated method stub
		return 3;
	}

	public static void main(String args[]){
		Inte3 acc = new TestIntr();
		System.out.println(acc.a());
		System.out.println(acc.b());
		System.out.println(acc.d());

	}

}
