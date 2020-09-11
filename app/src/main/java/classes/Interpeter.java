package classes;

import java.lang.String;

public class Interpeter {


    final String operators;


    String func;

    public Interpeter()
    {
        //proccesCode(func);
        operators = "^*/+-";
    }

    public double proccesCode(String rawFunc) {
        String func = rawFunc.toLowerCase();
        return eval(func);
    }
    public double eval(final String expRaw) {
        String exp = expRaw.trim().toLowerCase();
        double res;

        while(expRaw.contains("(") || exp.contains(")"))
        {
            int openBracket, closeBrecket;
            openBracket = exp.lastIndexOf('(');
            closeBrecket = exp.indexOf(')');
            Double tempRes = eval(exp.substring(openBracket,closeBrecket));
            exp = exp.substring(0,openBracket) + tempRes.toString() + exp.substring(closeBrecket,exp.length());
        }
        res = calculateExp(exp);
        return res;
    }

    private double calculateExp(String exp) {
        double res = 0;

        if (isOccurs(exp,operators) == -1)
            return Double.parseDouble(exp);
        //
        // ----------------------------------------- MUST BE FROM LEFT TO RIGHT!!!!!!!!!!!!!!!!!!!!!!!! -----------------------------------------------
        //
        exp = exp.replace("+-","-").trim().replaceAll("\\s+","");

        final String[] opOrder =new String[]{"^","*/","+-"};

        for(int i = 0; i < opOrder.length; i++)
        {
            String sign = opOrder[i];

            while (isOneContains(exp, sign.toCharArray()) ) {
                int signPlace = isOccurs(exp, sign.toCharArray());
                String beforeRaw = exp.substring(0, signPlace);
                String afterRaw = exp.substring(signPlace + 1);
                String before = beforeRaw, after = afterRaw;

                int beforeI = isOccursLast(beforeRaw, operators.toCharArray());
                if(beforeI != -1)
                {
                    //if(beforeRaw.charAt(beforeI) == '-') // For Minus before number.
                        //beforeI--;
                    before = beforeRaw.substring(beforeI + 1);
                }

                int afterI = isOccurs(afterRaw, operators.toCharArray());
                if(afterI != -1)
                    after = afterRaw.substring(0, afterI);

                switch (sign)
                {
                    case '^':
                    {
                        res = Math.pow(Double.parseDouble(before), Double.parseDouble(after));
                        break;
                    }
                    case '*':
                    {
                        res = Double.parseDouble(before) * Double.parseDouble(after);
                        break;
                    }
                    case '/':
                    {
                        if(Double.parseDouble(after) == 0)
                            throw new IllegalArgumentException("Argument 'divisor' is 0");
                        res = Double.parseDouble(before) / Double.parseDouble(after);
                        break;
                    }
                    case '+':
                    {
                        res = Double.parseDouble(before) + Double.parseDouble(after);
                        break;
                    }
                    case '-':
                    {
                        res = Double.parseDouble(before) - Double.parseDouble(after);
                        break;
                    }
                }
                StringBuilder temp = new StringBuilder();
                if(beforeI != -1)
                    temp.append(beforeRaw.substring(0,beforeI+1));
                temp.append(res);
                if(afterI != -1)
                    temp.append(afterRaw.substring(afterI));
                exp = temp.toString();
                System.out.println(exp);
            }
        }
        return Double.parseDouble(exp);
    }

    private boolean isOneContains(String exp, char[] arr) {
        boolean isOne = false;

        for (int i = 0; i < arr.length && !isOne; i++)
            isOne = exp.indexOf(arr[i]) != -1;


        return isOne;
    }

    private static int isOccurs(String str, char[] signs)
    {
        int isOccur = Integer.MAX_VALUE;
        for (int j = 0; j < signs.length && isOccur == Integer.MAX_VALUE; j++)
            isOccur = Math.min(str.indexOf(signs[j]) == -1 ? Integer.MAX_VALUE : str.indexOf(signs[j]) ,isOccur);

        return isOccur == Integer.MAX_VALUE ? -1 : isOccur;
    }
    private static int isOccursLast(String str, char[] signs)
    {
        int isOccur = -1;

        for (int j = 0; j < signs.length && isOccur == -1; j++)
            isOccur = Math.max(str.lastIndexOf(signs[j]),isOccur);

        return isOccur;
    }



}
