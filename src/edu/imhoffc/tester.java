package edu.imhoffc;

/**
 * Created by Eric on 9/28/2014.
 */
public class tester {
    public static void main(String[] args) {
        String[] thingy = {"--inputtype=utf8","--outputtype=STRING","'\\xC3\\xAA\\x74\\x72\\x65'"} ;
        try {
            Main.main(thingy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println((byte)Integer.parseInt("U+ABCD".substring(2,4),16));
    }
}
