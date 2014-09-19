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

    }

    @Test
    public void testToCodePointString() throws Exception {
        assertTrue("U+004C".equalsIgnoreCase(latinCapsL.toCodePointString()));
        assertTrue("U+2C6C".equalsIgnoreCase(latinSmallLetterZWithDescender.toCodePointString()));
        assertTrue("U+1D13D".equalsIgnoreCase(musicalSymbolQuarterRest.toCodePointString()));
    }

    @Test
    public void testToUTF8StringWithoutQuotes() throws Exception {

    }

    @Test
    public void testGetCharacterName() throws Exception {
        assertTrue("LATIN CAPITAL LETTER L".equalsIgnoreCase(latinCapsL.getCharacterName()));
        assertTrue("LATIN SMALL LETTER Z WITH DESCENDER".equalsIgnoreCase(latinSmallLetterZWithDescender.getCharacterName()));
        assertTrue("MUSICAL SYMBOL QUARTER REST".equalsIgnoreCase(musicalSymbolQuarterRest.getCharacterName()));
    }
}