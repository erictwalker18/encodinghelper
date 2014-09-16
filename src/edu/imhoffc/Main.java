package edu.imhoffc;

import java.util.*;

public class Main {

    public static void main(String[] args) {
	    System.out.println("Yo dawg.");
        Scanner in = new Scanner(System.in);

        System.out.println("What's your name?");
        String name = in.nextLine();

        System.out.println("Hey "+name);
        System.out.println();
        System.out.println();

        bark(5);
    }


    private static void bark(int times) {
        for(int i = 0; i<times; i++) {
            System.out.println("Woof");
        }
    }
}
