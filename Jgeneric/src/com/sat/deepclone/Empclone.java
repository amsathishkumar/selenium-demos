package com.sat.deepclone;

public class Empclone implements Cloneable {

    Sample s;
    int a;

    Empclone(int a, Sample s){
        this.a=a;
        this.s=s;

    }

public Object clone()throws CloneNotSupportedException{
       return new Empclone(this.a, new Sample(this.s.a,this.s.a));
}


public static void main(String[] args) {

        Empclone a= new Empclone(2, new Sample(3,3));
        Empclone b=null;

 try {
             b=(Empclone)a.clone();

 } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(a.s.a);
        System.out.println(b.s.a);

        a.s.a=12;
        System.out.println(a.s.a);
        System.out.println(b.s.a);
}

}