package classes;

public class Interpeter {


    final String operators;

    // Replacement for sign '-' which is not operator.
    final String subReplace;

    static {
        System.loadLibrary("native-lib");
    }



    String func;

    public Interpeter()
    {
        operators = "^*/+-";
        subReplace = "`";
    }

    public double calculate(String rawFunc) {
        String func = rawFunc.toLowerCase();
        return eval(func);
    }
    public double eval(final String expRaw) {
        String exp = expRaw
                .toLowerCase()
                .trim()
                .replaceAll("\\s+","")
                .replaceAll("((?=[-+*^/])[-])([-]{0})",subReplace);
                ;
        //System.out.println(exp);
        double res;

        while(exp.contains("(") || exp.contains(")"))
        {
            int openBracket, closeBracket;
            openBracket = exp.lastIndexOf('(');
            closeBracket = exp.indexOf(')');
            Double tempRes = eval(exp.substring(openBracket + 1,closeBracket));
            exp = exp.substring(0,openBracket) + tempRes.toString() + exp.substring(closeBracket+1);
        }
        res = calculateExp(exp);
        return res;
    }

    private double calculateExp(String exp) {
        double res = 0;

        // If no signs parse and exit.
        if (isOccurs(exp,operators.toCharArray()) == -1)
            return Double.parseDouble(exp);

        // Remove "+-" strings.
        //exp = exp.replace("+-","-");

        // Order of signs.
        final String[] opOrder = new String[]{"^","*/","+-"};


        // Looping for each couple signs.
        for(int i = 0; i < opOrder.length; i++)
        {
            // Get current sign.
            String signs = opOrder[i];

            // While it's still not exp and include signs.
            while (!tryParseDouble(exp.replaceAll(subReplace,"-")) && isOneContains(exp, signs.toCharArray()) ) {
                // Get sign place.
                int signPlace = isOccurs(exp, signs.toCharArray());
                // Get actual sign.
                char sign = exp.charAt(signPlace);

                // Get string before the sign.
                String beforeRaw = exp.substring(0, signPlace);
                // Get string after the sign.
                String afterRaw = exp.substring(signPlace + 1);
                // So we can change them.
                String before = beforeRaw, after = afterRaw;

                // Check if there is sign before our sign.
                int beforeI = isOccursLast(beforeRaw, operators.toCharArray());
                // If there is sign before.
                if(beforeI != -1) {
                    // If it's '-' and it's the first, so it's negative sign.
                    //if(beforeRaw.charAt(beforeI) == '-' && beforeI == 0) // For Minus before number.
                     //   beforeI--;
                    // Get the string after the sign.
                    before = beforeRaw.substring(beforeI + 1);
                }

                // Check if sign after.
                int afterI = isOccurs(afterRaw, operators.toCharArray());
                // If it's minus sign of number.
                //if(afterI == 0 && afterRaw.charAt(afterI) == '-')
                //    afterI = isOccurs(afterRaw.substring(1),operators.toCharArray());
                // If sign exists.
                if(afterI != -1) {
                    // Get the string before it.
                    after = afterRaw.substring(0, afterI);
                }

                before =  before.replaceAll(subReplace,"-");
                after =  after.replaceAll(subReplace,"-");

                // Only if there is before and after, and not "+2".
                if(after != "" && before != "") {
                    //Check What is the sign.
                    switch (sign) {
                        case '^': {
                            res = Math.pow(Double.parseDouble(before), Double.parseDouble(after));
                            break;
                        }
                        case '*': {
                            res = Double.parseDouble(before) * Double.parseDouble(after);
                            break;
                        }
                        case '/': {
                            if (Double.parseDouble(after) == 0)
                                throw new IllegalArgumentException("Argument 'divisor' is 0");
                            res = Double.parseDouble(before) / Double.parseDouble(after);
                            break;
                        }
                        case '+': {
                            res = Double.parseDouble(before) + Double.parseDouble(after);
                            break;
                        }
                        case '-': {
                             res = Double.parseDouble(before) - Double.parseDouble(after);
                            break;
                        }
                    } // Calculate.
                }
                else
                {
                    // Parse the exp.
                    res = Double.parseDouble(before + after);
                }
                String resStr = String.valueOf(res).replaceAll("((?=[-+*^/])[-])([-]{0})",subReplace);

                // For string addition.
                StringBuilder temp = new StringBuilder();
                // If there is string before the sign.
                if(beforeI != -1)
                    temp.append(beforeRaw.substring(0,beforeI+1));
                // Added the result.
                temp.append(res);
                // If there is string after the sign.
                if(afterI != -1)
                    temp.append(afterRaw.substring(afterI));
                // Parse to string.
                exp = temp.toString().replaceAll("((?=[-+*^/])[-])([-]{0})",subReplace);
                //System.out.println(exp);
            }
        }

        // Parse the exp because no signs involved:)
        return Double.parseDouble(exp.replace(subReplace,"-"));
    }

    // Function to check if string can parse as double.
    private boolean tryParseDouble(String exp) {
        try {
            double d = Double.parseDouble(exp);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    // Check if string contains one char of char array.
    private boolean isOneContains(String exp, char[] arr) {
        boolean isOne = false;

        for (int i = 0; i < arr.length && !isOne; i++)
            isOne = exp.indexOf(arr[i]) != -1;

        return isOne;
    }

    // Return the index of closest char of char array to start, return '-1' if not found.
    private static int isOccurs(String str, char[] signs)
    {
        int isOccur = Integer.MAX_VALUE;
        for (int j = 0; j < signs.length; j++)
            isOccur = Math.min(str.indexOf(signs[j]) == -1 ? Integer.MAX_VALUE : str.indexOf(signs[j]) ,isOccur);

        return isOccur == Integer.MAX_VALUE ? -1 : isOccur;
    }

    // Return the index of closest char of char array to end, return '-1' if not found.
    private static int isOccursLast(String str, char[] signs)
    {
        int isOccur = -1;

        for (int j = 0; j < signs.length; j++)
            isOccur = Math.max(str.lastIndexOf(signs[j]),isOccur);

        return isOccur;
    }

}
