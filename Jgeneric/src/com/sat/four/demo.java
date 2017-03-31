package com.sat.four;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Most frequently visited 3-page path - Path taken by user madeup of 3 url
//
//[/p/1, /p/2, /p/2/add_to_cart]
//[/p/2, /p/2/add_to_cart, /cart]
//[/p/2/add_to_cart, /cart, /payment]
//[/p/1, /faq, /customer_support]

public class demo {
	public static void main(String args[]) {
		System.out.println("Extract MAX 3 Page URL:");
		ArrayList<String> logvalues = readthelog();
		System.out.println(logvalues);
		System.out.println("RESULT:");
		print3Pagemax(logvalues);

	}

	public static void print3Pagemax(ArrayList<String> logvalues) {
		Map<String, ArrayList<String>> userwisectionvalues = userwisection(logvalues);
		Map<String, String> user_3page = user3page_gen(userwisectionvalues);
		int max = 0;
		String max3page = "";
		for (String keyv : user_3page.keySet()) {
			if (Integer.parseInt(user_3page.get(keyv)) > max) {
				max = Integer.parseInt(user_3page.get(keyv));
				max3page = keyv;
			}
		}
		System.out.println("\n\n Most frequently visited 3-page path is:"
				+ max3page + " count:" + max);
	}

	private static Map<String, String> user3page_gen(
			Map<String, ArrayList<String>> userwisectionvalues) {
		Map<String, String> page3 = new HashMap<String, String>();
		for (String keyv : userwisectionvalues.keySet()) {
			ArrayList<String> value = userwisectionvalues.get(keyv);
			for (int i = 0; i < value.size(); i++) {
				String page3Key = "";
				for (int j = i; j < i + 3; j++) {
					if (j < value.size())
						page3Key = page3Key + value.get(j) + ",";
					else
						break;
				}
				if (page3.containsKey(page3Key)) {
					int incr = Integer.parseInt(page3.get(page3Key)) + 1;
					page3.put(page3Key, String.valueOf(incr));
				} else
					page3.put(page3Key, "0");
			}

		}
		return page3;

	}

	private static Map<String, ArrayList<String>> userwisection(
			ArrayList<String> logvalues) {
		Map<String, ArrayList<String>> userwisectionlist = new HashMap<String, ArrayList<String>>();
		for (String logvalue : logvalues) {
			String spvalues[] = logvalue.split(",");
			if (userwisectionlist.containsKey(spvalues[0])) {
				ArrayList<String> al1 = userwisectionlist.get(spvalues[0]);
				al1.add(spvalues[1].trim());
				userwisectionlist.put(spvalues[0].trim(), al1);

			} else {
				ArrayList<String> al1 = new ArrayList<String>();
				al1.add(spvalues[1].trim());
				userwisectionlist.put(spvalues[0].trim(), al1);
			}
		}

		for (String key : userwisectionlist.keySet())
			System.out.println("UserId:" + key + "\n   Navigate Path:"
					+ userwisectionlist.get(key));

		return userwisectionlist;
	}

	private static ArrayList<String> readthelog() {
		ArrayList<String> navigatePath = new ArrayList<String>();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(
					"C:\\Users\\smuniapp\\git\\selenium-demos\\Jgeneric\\src\\com\\sat\\four\\log.csv");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				navigatePath.add(strLine.trim());
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return navigatePath;
	}

}
