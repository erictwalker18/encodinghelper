package edu.imhoffc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EncodingHelperCharTest {

    private EncodingHelperChar latinCapsL; //ascii + BMP, U+004C, int code point 76
    private EncodingHelperChar latinSmallLetterZWithDescender; //BMP, U+2C6C, int code point 11372
    private EncodingHelperChar musicalSymbolQuarterRest; //non-BMP, U+1D13D, int code point 119101

    @Before
    public void setUp() throws Exception {
        //uses the most basic EncodingHelpChar constructor
        this.latinCapsL = new EncodingHelperChar(76);
        this.latinSmallLetterZWithDescender = new EncodingHelperChar(11372);
        this.musicalSymbolQuarterRest = new EncodingHelperChar(119101);
    }

    @After
    public void tearDown() throws Exception {
        //I guess if we use a Scanner for reading out Unicode file we'd close that here...
        // but it's probably limited into the EncodingHelpChar's scope, not this scope.
    }

    @Test
    public void testGetCodePoint() throws Exception {
        assertEquals(76,latinCapsL.getCodePoint());
        assertEquals(11372, latinSmallLetterZWithDescender.getCodePoint());
        assertEquals(119101,musicalSymbolQuarterRest.getCodePoint());
    }

    @Test
    public void testSetCodePoint() throws Exception {
        EncodingHelperChar mutableChar = new EncodingHelperChar(76);    //create as latin caps L
        mutableChar.setCodePoint(11372);                                //set to latin small z with descender

        assertEquals(11372,mutableChar.getCodePoint());
    }

    @Test
    public void testToUTF8Bytes() throws Exception {
        byte[] latinCapsLBytes = {b(76)};
            //just 76 because it's ascii
        assertArrayEquals(latinCapsLBytes, latinCapsL.toUTF8Bytes());

        byte[] latinSmallLetterZWithDescenderBytes = {b(226), b(177), b(172)};
        assertArrayEquals(latinSmallLetterZWithDescenderBytes, latinSmallLetterZWithDescender.toUTF8Bytes());

        byte[] musicalSymbolQuarterRestBytes = {b(240), b(157), b(132), b(189)};
        assertArrayEquals(musicalSymbolQuarterRestBytes, musicalSymbolQuarterRest.toUTF8Bytes());
    }

    //a simple helper method for all the byte downcasting down in the above test.
    private byte b(int i) {
        return (byte) i;
    }

    @Test
    public void testToCodePointString() throws Exception {
        assertEquals("U+004C",latinCapsL.toCodePointString());
        assertEquals("U+2C6C",latinSmallLetterZWithDescender.toCodePointString());
        assertEquals("U+1D13D",musicalSymbolQuarterRest.toCodePointString());
    }

    @Test
    public void testToUTF8StringWithoutQuotes() throws Exception {
        //an extra backslash in included in these String literals
        // otherwise Java would attempt to interpret them as their Unicode conversion
        assertEquals("\\x4C",latinCapsL.toUTF8StringWithoutQuotes());
        assertEquals("\\xE2\\xB1\\xAC",latinSmallLetterZWithDescender.toUTF8StringWithoutQuotes());
        assertEquals("\\xF0\\x9D\\x84\\xBD",musicalSymbolQuarterRest.toUTF8StringWithoutQuotes());
    }

    @Test
    public void testGetCharacterName() throws Exception {
        assertEquals("LATIN CAPITAL LETTER L",latinCapsL.getCharacterName());
        assertEquals("LATIN SMALL LETTER Z WITH DESCENDER",latinSmallLetterZWithDescender.getCharacterName());
        assertEquals("MUSICAL SYMBOL QUARTER REST",musicalSymbolQuarterRest.getCharacterName());
    }
}