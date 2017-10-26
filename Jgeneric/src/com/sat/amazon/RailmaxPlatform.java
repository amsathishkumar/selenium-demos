package com.sat.amazon;
import java.util.TreeMap;
//Minimum Number of Platforms Required for a Railway/Bus Station
class RailmaxPlatform {
	public static void main(String args[]){
		double arr[]  = {9.00,  9.40, 9.50,  11.00, 15.00, 18.00};
		double dep[]  = {9.10, 12.00, 11.20, 11.30, 19.00, 20.00};
		System.out.println("Max Platform===>"+ maxplatform(arr, dep));
  }

	private static int maxplatform(double[] arr, double[] dep) {
//TreeMap will be sorted by key
		TreeMap<Double, String> arrdep = new TreeMap <Double,String>();
//All events sorted by time.
        for (int i=0;i<arr.length;i++)
        	arrdep.put(arr[i], "Arrival");

        for (int i=0;i<dep.length;i++)
        	arrdep.put(dep[i], "Dept");

//Total platforms at any time can be obtained by subtracting total
//departures from total arrivals by that time.
        int platform=0;
        int maxplatform=0;
        for (double key : arrdep.keySet()){

        	if (arrdep.get(key).equals("Arrival")){
        		platform ++;
        	}
        	else{
        		if (platform > maxplatform)
        			maxplatform=platform;
        		platform --;
        		}
         System.out.println("key:" + key + "value: "+arrdep.get(key) + " max Platform:"+ platform);

        }
        return maxplatform;
	}
}
