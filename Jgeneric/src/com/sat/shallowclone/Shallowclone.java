package com.sat.shallowclone;

public class Shallowclone implements Cloneable {

    Sample e;
    int a;

    Shallowclone (int a, Sample es){
        this.a=a;
        this.e=es;

    }

 public Object clone()throws CloneNotSupportedException{

        return super.clone();
 }


public static void main(String[] args) {

	Shallowclone a= new Shallowclone (2, new Sample(3,3));
	Shallowclone b=null;

        try {
             b=(Shallowclone )a.clone();

        } catch (CloneNotSupportedException e) {

            e.printStackTrace();
        }
        System.out.println(a.e.a);
        System.out.println(b.e.a);

        a.e.a=12;
        System.out.println(a.e.a);
        System.out.println(b.e.a);
    }

}