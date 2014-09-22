package edu.imhoffc;

import org.junit.Test;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        String a = Character.toChars(0x20AC)[0]+"";
        System.out.println(a);
	    System.out.println("Yo dawg.");
        Scanner in = new Scanner(System.in);

        System.out.println("What's your name?");
        String name = in.nextLine();

        System.out.println("Hey "+name);
        System.out.println();
        System.out.println();
    }
}
