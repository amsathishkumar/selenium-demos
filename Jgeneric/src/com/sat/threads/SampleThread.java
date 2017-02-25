package com.sat.threads;

class srun extends Thread {
	@Override
	public void run() {
		System.out.println("sathish");
		for (int i = 0; i < 10000; i++) {
            System.out.println("i value"+ String.valueOf(i));
            try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

public class SampleThread {
	public static void main(String[] args) throws InterruptedException {
		srun s1 = new srun();
		s1.start();
		while (s1.isAlive()){
			System.out.println("main");
			Thread.sleep(1000);
		}
		
		
	}

}
