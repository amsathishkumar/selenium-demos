package com.sat.amazon;

public class Train {
	public static void main(String arg[]){
		int arr[] = new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
		int left[] = new int [arr.length];
		int right[] = new int [arr.length];
		int n=arr.length;

		left[0] = arr[0];
        for (int i = 1; i < n; i++)
           left[i] = Math.max(left[i-1], arr[i]);

        right[n-1] = arr[n-1];
        for (int i = n-2; i >= 0; i--)
           right[i] = Math.max(right[i+1], arr[i]);

        for (int i=0;i<n;i++)
        	System.out.println("Left:"+ left[i] +" Right:" + right[i] + " min:" + Math.min(left[i],right[i])+" difference"+ (Math.min(left[i],right[i]) - arr[i]));

        int water = 0;
        for (int i = 0; i < n; i++)
            water += Math.min(left[i],right[i]) - arr[i];

        System.out.println("Water hold:"+water);

	}

}
