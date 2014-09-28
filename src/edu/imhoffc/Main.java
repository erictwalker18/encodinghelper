package edu.imhoffc;

import org.junit.Test;

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
                System.out.println(data);
            }
        }

        //----- PART 2: Read the data into EncodingHelperChars

        //will hold the helpful nuggets of computation we made in phase_2
        ArrayList<EncodingHelperChar> unicodeCharacters = new ArrayList<EncodingHelperChar>();
        //an array in case they passed in multiple letters

        if (data != null) {
            //Use a switch statement on 'inType' to read the input data correctly
            switch (inType) {
                case STRING:
                    for (int i=0 ; i<data.length(); i++) {
                        unicodeCharacters.add(new EncodingHelperChar(data.charAt(i)));
                    }
                    break;
                case UTF8:
                    byte[] myBytes = new byte[(data.length()-2)/4];
                    for (int i = 0; i < data.length(); i++) {
                        myBytes[i] = Byte.parseByte(data.substring(4*i+3, 4*i+5),16);
                    }
                    unicodeCharacters.add(new EncodingHelperChar(myBytes));
                    break;
                case CODEPOINT:
                    unicodeCharacters.add(new EncodingHelperChar(Integer.parseInt(data.substring(2),16)));
                    break;
            }
        } else {
            //do whatever it is we're supposed to do when we don't get any real input
            //I don't know what we're supposed to do when we don't get real input

        }

        //----- Part 3: Return the data the user wants

        //Use a switch statement on 'outType' to output the requested data
        switch (outType) {
            case SUMMARY:
                if(unicodeCharacters.size() == 1) {
                    //do full summary
                    System.out.println(
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
                        written = " " + new String(c.toUTF8Bytes(), "UTF-8");
                        codepoints+= " " + c.toCodePointString();
                        utf8+= c.toUTF8StringWithoutQuotes();
                    }
                    System.out.println(
                            "String:" + written +
                            "\nCode Points:" + codepoints +
                            "\nUTF-8: " + utf8);
                }

                break;
            case STRING:
                System.out.println(unicodeCharacters.size());
                for (EncodingHelperChar c: unicodeCharacters)
                    System.out.print(new String(c.toUTF8Bytes(), "UTF-8"));
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


}
