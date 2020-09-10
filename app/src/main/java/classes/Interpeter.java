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


        exp = exp.replace("+-","-").trim().replaceAll("\\s+","");

        for(int i = 0; i < operators.length(); i++)
        {
            char sign = operators.charAt(i);
            while (exp.contains(operators.substring(i, i + 1)) ) {
                int signPlace = exp.indexOf(sign);
                String before = exp.substring(0, signPlace);
                String after = exp.substring(signPlace + 1);

                int beforeI = isOccursLast(before, operators);
                if(beforeI != -1)
                {
                    if(before.charAt(beforeI) == '-') // For Minus before number.
                        beforeI--;
                    before = before.substring(beforeI + 1);

                }

                int afterI = isOccurs(after, operators);
                if(afterI != -1)
                    after = after.substring(0, afterI);

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
                    temp.append(exp.substring(0,beforeI+1));
                temp.append(res);
                if(afterI != -1)
                    temp.append(exp.substring(afterI+2));
                exp = temp.toString();
                System.out.println(exp);
            }
        }
        return Double.parseDouble(exp);
    }

    private static int isOccurs(String str, CharSequence signs)
    {
        int isOccur = Integer.MAX_VALUE;
        for (int j = 0; j < signs.length(); j++)
            isOccur = Math.min(str.indexOf(signs.charAt(j)),isOccur);

        return isOccur == Integer.MAX_VALUE ? -1 : isOccur;
    }
    private static int isOccursLast(String str, CharSequence signs)
    {
        int isOccur = -1;

        for (int j = 0; j < signs.length(); j++)
            isOccur = Math.max(str.lastIndexOf(signs.charAt(j)),isOccur);

        return isOccur;
    }



}
