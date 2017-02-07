/**
 * 
 */
package com.sat.abst;

/**
 * @author smuniapp
 *
 */
public abstract class firstabst {
	protected String fname;
	protected  String lname;
	public String getName()
	{
		return fname + lname;
		
	}
	public abstract void setfName(String fname);
	public abstract void setLName(String lname);

}
