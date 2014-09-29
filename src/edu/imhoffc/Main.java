package edu.imhoffc;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        //----- PART 1: Set up using inputted flags

        //the inputed char, codepoint, string, or bytes that we'll do calculations on
        String data = null;
        //defined here for the extra bit of scope, but might be assigned via an arg while scanning the input
        //starts as null because it may be that the user does not input an arg for data

        InputType inType = InputType.STRING;  //by default
        boolean assignedInType = false;
        OutputType outType = OutputType.SUMMARY; //by default
        boolean assignedOutType = false;

        //if no arguments are given, then the standard input is used
        if (args.length == 0) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                args = br.readLine().split(" ");
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        //if there are still no arguments, then the program exits with a useful error message
        if (args.length==0) {
            System.err.print("No arguments entered. Please try again.");
        }

        //scan for flags
        for (int i=0; i<args.length; i++) {
            String currentToken = args[i];
            if (currentToken.startsWith("--inputtype=")) {
                //check if an inputtype flag has already been read
                if (assignedInType) {
                    System.out.println("Invalid Args: Multiple --inputtype flags.");
                    return;
                }
                
                //assign inType
                String flagValue = currentToken.replaceFirst("--inputtype=","");
                inType = getInputTypeFromString(flagValue);
                assignedInType = true;

                //ensure its not invalid
                if (inType == InputType.INVALID) {
                    System.out.println("Invalid input type: " + flagValue +
                            "\n The input types are: " +
                            "\n String," +
                            "\n UTF8," +
                            "\n Codepoint");
                    return;
                }

            }
            else if (currentToken.startsWith("--outputtype=")) {
                //check if an outputtype flag has already been read
                if (assignedOutType) {
                    System.out.println("Invalid Args: Multiple --outputtype flags.");
                    return;
                }
                
                //assign outType
                String flagValue = currentToken.replaceFirst("--outputtype=","");
                outType = getOutputTypeFromString(flagValue);
                assignedOutType = true;

                //ensure its not invalid
                if (outType == OutputType.INVALID) {
                    System.out.println("Invalid output type: " + flagValue +
                            "\n The output types are: " +
                            "\n Summary," +
                            "\n String," +
                            "\n UTF8," +
                            "\n Codepoint");
                }
            }
            else {
                data = args[i];
            }
        }

        //----- PART 2: Read the data into EncodingHelperChars

        //will hold the helpful nuggets of computation we made in phase_2
        ArrayList<EncodingHelperChar> unicodeCharacters = new ArrayList<EncodingHelperChar>();
        //an array in case they passed in multiple letters

        //Use a switch statement on 'inType' to read the input data correctly
        switch (inType) {
            case STRING:
                for (int i=0 ; i<data.length(); i++) {
                    unicodeCharacters.add(new EncodingHelperChar(data.charAt(i)));
                }
                break;
            case UTF8:
                ArrayList<Byte> bytesInChar = new ArrayList<Byte>();
                boolean isFirstInSeq = true;
                int numBytesLeft = 0;
                for (int i = 0; i < (data.length()-2)/4; i++) {
                    Byte thisByte = Integer.decode("0x" + data.substring(4 * i + 3, 4 * i + 5)).byteValue();
                    if (isFirstInSeq) {
                        bytesInChar.clear();
                        numBytesLeft = getNumBytesInSeq(thisByte)-1;
                        isFirstInSeq = false;
                    }
                    else {
                        numBytesLeft--;
                    }
                    bytesInChar.add(thisByte);
                    if (numBytesLeft == 0) {
                        byte[] out = new byte[bytesInChar.size()];
                        for (int j = 0; j<bytesInChar.size(); j++) {
                            out[j] = (byte)bytesInChar.get(j);
                        }
                        unicodeCharacters.add(new EncodingHelperChar(out));
                        isFirstInSeq = true;
                    }
                }
                break;
            case CODEPOINT:
                unicodeCharacters.add(new EncodingHelperChar(Integer.parseInt(data.substring(2),16)));
                break;
        }

        //----- Part 3: Return the data the user wants

        //Use a switch statement on 'outType' to output the requested data
        switch (outType) {
            case SUMMARY:
                if(unicodeCharacters.size() == 1) {
                    //do full summary
                    System.out.print(
                            "Character: " + new String(unicodeCharacters.get(0).toUTF8Bytes(),"UTF-8") +
                            "\nCode Point: " + unicodeCharacters.get(0).toCodePointString() +
                            "\nName: " + unicodeCharacters.get(0).getCharacterName() +
                            "\nUTF-8: " + unicodeCharacters.get(0).toUTF8StringWithoutQuotes());

                } else {
                    //do shorter summary
                    String written = "";
                    String codepoints = "";
                    String utf8 = "";
                    for (EncodingHelperChar c: unicodeCharacters) {
                        written += new String(c.toUTF8Bytes(), "UTF-8");
                        codepoints+= " " + c.toCodePointString();
                        utf8+= c.toUTF8StringWithoutQuotes();
                    }
                    System.out.print(
                            "String: " + written +
                            "\nCode Points:" + codepoints +
                            "\nUTF-8: " + utf8);
                }

                break;
            case STRING:
                for (EncodingHelperChar c: unicodeCharacters) {
                    System.out.print(new String(c.toUTF8Bytes(), "UTF-8"));
                }
                break;
            case UTF8:
                for (EncodingHelperChar c: unicodeCharacters)
                    System.out.print(c.toUTF8StringWithoutQuotes());
                break;
            case CODEPOINT:
                for (int i = 0; i < unicodeCharacters.size(); i++) {
                    System.out.print(unicodeCharacters.get(i).toCodePointString());
                    if (i<unicodeCharacters.size()-2)
                        System.out.print(" ");
                }
                break;
        }
    }




    //Enum stuff (and fancy enum factories):
    private enum InputType {
        STRING, UTF8, CODEPOINT,
        INVALID
    }

    private static InputType getInputTypeFromString(String s) {
        if (s.equalsIgnoreCase("string")) {
            return InputType.STRING;
        } else if (s.equalsIgnoreCase("utf8") || (s.equalsIgnoreCase("utf-8"))) {
            return InputType.UTF8;
        } else if (s.equalsIgnoreCase("codepoint")) {
            return InputType.CODEPOINT;
        } else {
            return InputType.INVALID;
        }
    }

    private enum OutputType {
        SUMMARY, STRING, UTF8, CODEPOINT,
        INVALID
    }

    private static OutputType getOutputTypeFromString(String s) {
        if (s.equalsIgnoreCase("summary")) {
            return OutputType.SUMMARY;
        } else if (s.equalsIgnoreCase("string")) {
            return OutputType.STRING;
        } else if (s.equalsIgnoreCase("utf8") || (s.equalsIgnoreCase("utf-8"))) {
            return OutputType.UTF8;
        } else if (s.equalsIgnoreCase("codepoint")) {
            return OutputType.CODEPOINT;
        } else {
            return OutputType.INVALID;
        }
    }

    private static int getNumBytesInSeq(byte firstByte) {
        if (firstByte<-64||firstByte>0) {
            return 1;
        }
        else if (firstByte<-32) {
            return 2;
        }
        else if (firstByte<-16) {
            return 3;
        }
        else if (firstByte<-8) {
            return 4;
        }
        else if (firstByte<-4) {
            return 5;
        }
        else {
            return 6;
        }
    }


}
