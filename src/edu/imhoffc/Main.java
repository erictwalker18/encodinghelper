package edu.imhoffc;

import org.junit.Test;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        //----- PART 1: Set up using inputted flags

        //the inputed char, codepoint, string, or bytes that we'll do calculations on
        String data = null;
        //defined here for the extra bit of scope, but might be assigned via an arg while scanning the input
        //starts as nil because it may be that the user does not input an arg for data

        InputType inType = InputType.STRING;  //by default
        boolean assignedInType = false;
        OutputType outType = OutputType.SUMMERY; //by default
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
                String flagValue = currentToken.replaceFirst("--inputtype","");
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
                String flagValue = currentToken.replaceFirst("--outputtype","");
                outType = getOutputTypeFromString(flagValue);
                assignedOutType = true;

                //ensure its not invalid
                if (outType == OutputType.INVALID) {
                    System.out.println("Invalid output type: " + flagValue +
                            "\n The output types are: " +
                            "\n Summery," +
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

        if (data != null) {
            //Use a switch statement on 'inType' to read the input data correctly
            switch (inType) {
                case STRING:
                    for (int i=0 ; i<data.length(); i++) {
                        unicodeCharacters.add(new EncodingHelperChar(data.charAt(i)));
                    }
                    break;
                case UTF8:

                    break;
                case CODEPOINT:

                    break;
            }
        } else {
            //do whatever it is we're supposed to do when we don't get any real input

        }

        //----- Part 3: Return the data the user wants

        //Use a switch statement on 'outType' to output the requested data
        switch (outType) {
            case SUMMERY:
                if(unicodeCharacters.size() == 1) {
                    //do full summery

                } else {
                    //do shorter summery
                }

                break;
            case STRING:

                break;
            case UTF8:

                break;
            case CODEPOINT:

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
        SUMMERY, STRING, UTF8, CODEPOINT,
        INVALID
    }

    private static OutputType getOutputTypeFromString(String s) {
        if (s.equalsIgnoreCase("summery")) {
            return OutputType.SUMMERY;
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
