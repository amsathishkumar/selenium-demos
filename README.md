# selenium-demos
###demo1
window / frame handling/ run java with maven
```sh
goal:clean test
```
###Election
 To build executable jar files
```sh
goal: clean package assembly:single
```

###Jgeneric
* What are Generics? 
Generics are used to create Generic Classes and Generic methods which can work with different Types(Classes).

###JAVA collection

A Collection is a group of individual objects represented as a single unit. Java provides Collection Framework which defines several classes and interfaces to represent a group of objects as a single unit.

The Collection interface (java.util.Collection) and Map interface (java.util.Map) are two main root interfaces of Java collection classes.


Before Collection Framework (or before JDK 1.2) was introduced, the standard methods for grouping Java objects (or collections) were array or Vector or Hashtable. All three of these collections had no common interface.

For example, if we want to access elements of array, vector or Hashtable. All these three have different methods and syntax for accessing members:
```sh
// Java program to show whey collection framework was needed
import java.io.*;
import java.util.*;
 
class Test
{
    public static void main (String[] args)
    {
        // Creating instances of array, vector and hashtable
        int arr[] = new int[] {1, 2, 3, 4};
        Vector<Integer> v = new Vector();
        Hashtable<Integer, String> h = new Hashtable();
        v.addElement(1);
        v.addElement(2);
        h.put(1,"sat");
        h.put(2,"kumar");
 
        // Array instance creation requires [], while Vector
        // and hastable require ()
        // Vector element insertion requires addElement(), but
        // hashtable element insertion requires put()
 
        // Accessing first element of array, vector and hashtable
        System.out.println(arr[0]);
        System.out.println(v.elementAt(0));
        System.out.println(h.get(1));
 
        // Array elements are accessed using [], vector elements
        // using elementAt() and hashtable elements using get()
    }
}

OUTPUT
1
1
sat
```
