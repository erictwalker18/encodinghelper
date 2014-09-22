package edu.imhoffc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * EncodingHelperChar
 *
 * The main support class for the EncodingHelper project in
 * CS 257, Fall 2014, Carleton College. Each object of this class
 * represents a single Unicode character. The class's methods
 * generate various representations of the character.
 */

public class EncodingHelperChar {
    private int codePoint;

    public EncodingHelperChar(int codePoint) {
        this.codePoint = codePoint;
    }

    public EncodingHelperChar(byte[] utf8Bytes) {
        try {
            String text = new String(utf8Bytes, "UTF-8");
            char ch = text.charAt(0);

            //now just init with char
            this.codePoint = getCodePointFromChar(ch);

        } catch (Exception e) {
            System.err.println("invalid byte array");
        }
    }

    public EncodingHelperChar(char ch) {
        this.codePoint = getCodePointFromChar(ch);
    }

    //helper method
    private static int getCodePointFromChar(char ch) {
        //Integer has a nice function to convert Unicode chars directly to their Hex index,
        // but none to convert directly to Base 10...
        String hexValue = Integer.toHexString(ch);
        return Integer.parseInt(hexValue, 16);    //parse the hexValue String from base 16 into int (base 10)
    }

    public int getCodePoint() {
        return this.codePoint;
    }

    public void setCodePoint(int codePoint) {
        this.codePoint = codePoint;
    }

    /**
     * Converts this character into an array of the bytes in its UTF-8 representation.
     * For example, if this character is a lower-case letter e with an acute accent,
     * whose UTF-8 form consists of the two-byte sequence C3 A9, then this method returns
     * a byte[] of length 2 containing C3 and A9.
     *
     * @return the UTF-8 byte array for this character
     */
    @Deprecated
    public byte[] toUTF8BytesOld() throws Exception {
        System.out.println("Starting"+this.codePoint);
        String tempString = Integer.toBinaryString(this.codePoint);
        int a=tempString.length()-7;
        //System.out.println((byte)(Byte.valueOf(tempString.substring(a - 6, a), 2) + 128));
        if (this.codePoint<0x7F)
            return new byte[]{(byte)this.codePoint};
        else if(this.codePoint<0x7FF)
            return new byte[]{(byte)(Byte.valueOf(tempString.substring(0,a),2)+193),(byte)(Byte.valueOf(tempString.substring(a),2)+128)};
        else if(this.codePoint<0xFFFF)
            return new byte[]{(byte) (Byte.valueOf(tempString.substring(0,a-6),2)+225),(byte)(Byte.valueOf(tempString.substring(a - 6, a), 2) + 128), (byte) (Byte.valueOf(tempString.substring(a), 2) + 128)};
        else if(this.codePoint<0x1FFFFF)
            return new byte[]{(byte)(Byte.valueOf(tempString.substring(0,a-12),2)+241),(byte)(Byte.valueOf(tempString.substring(a-12,a-6),2)+128),(byte)(Byte.valueOf(tempString.substring(a-6,a),2)+128),(byte)(Byte.valueOf(tempString.substring(a),2)+128)};
        else if(this.codePoint<0x3FFFFFF)
            return new byte[]{(byte)(Byte.valueOf(tempString.substring(0,a-18),2)+249),(byte)(Byte.valueOf(tempString.substring(a-18,a-12),2)+128),(byte)(Byte.valueOf(tempString.substring(a-12,a-6),2)+128),(byte)(Byte.valueOf(tempString.substring(a-6,a),2)+128),(byte)(Byte.valueOf(tempString.substring(a),2)+128)};
        else if(this.codePoint<0x7FFFFFFF)
            return new byte[]{(byte)(Byte.valueOf(tempString.substring(0,a-24),2)+253),(byte)(Byte.valueOf(tempString.substring(a-24,a-18),2)+128),(byte)(Byte.valueOf(tempString.substring(a-18,a-12),2)+128),(byte)(Byte.valueOf(tempString.substring(a-12,a-6),2)+128),(byte)(Byte.valueOf(tempString.substring(a-6,a),2)+128),(byte)(Byte.valueOf(tempString.substring(a),2)+128)};
        else
            return new byte[]{};
    }

    public byte[] toUTF8Bytes() throws Exception {
        char characterRepresentation = (char) this.codePoint;
        String stringRepresentation = ""+characterRepresentation;
        return stringRepresentation.getBytes("UTF-8");
    }

    /**
     * Generates the conventional 4-digit hexadecimal code point notation for this character.
     * For example, if this character is a lower-case letter e with an acute accent,
     * then this method returns the string U+00E9 (no quotation marks in the returned String).
     *
     * @return the U+ string for this character
     */
    public String toCodePointString() {
        return String.format("U+%04x",this.codePoint).toUpperCase();    //formats the string in hex form with leading 0s
    }

    /**
     * Generates a hexadecimal representation of this character suitable for pasting into a
     * string literal in languages that support hexadecimal byte escape sequences in string
     * literals (e.g. C, C++, and Python). For example, if this character is a lower-case
     * letter e with an acute accent (U+00E9), then this method returns the
     * string \xC3\xA9. Note that quotation marks should not be included in the returned String.
     *
     * @return the escaped hexadecimal byte string
     */
    public String toUTF8StringWithoutQuotes() throws Exception {
        byte[] in = toUTF8Bytes();
        String out = "";
        for(byte b: in) {
            out += "\\x"+String.format("%x",b).toUpperCase();     //converts each byte to hex and formats it into a proper String
        }
        return out;
    }

    /**
     * Generates the official Unicode name for this character.
     * For example, if this character is a lower-case letter e with an acute accent,
     * then this method returns the string "LATIN SMALL LETTER E WITH ACUTE" (without quotation marks).
     *
     * @return this character's Unicode name
     */
    public String getCharacterName() {

        //special cases the char is a private use codepoint:
        if ((this.codePoint >= 0xE000 && this.codePoint <= 0xF8FF) ||           //private use section of BMP
                (this.codePoint >= 0xF0000 && this.codePoint <= 0xFFFFD) ||         //private use plane 15
                    (this.codePoint >= 0x100000 && this.codePoint <= 0x10FFFD)) {       //private use plane 16
            return String.format("Private Use Codepoint : "+this.toCodePointString());
        }

        //else look for it in the UnicodeData.txt list
        try {
            File file = new File("UnicodeData.txt");
            Scanner scanner = new Scanner(file);    //file scanner
            while(scanner.hasNextLine()) {
                Scanner scan = new Scanner(scanner.nextLine());     //line scanner
                scan.useDelimiter(";");
                if(scan.next().equalsIgnoreCase(toCodePointString().substring(2))) {
                    return scan.next();
                }
                scan.close();
            }
            scanner.close();
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}