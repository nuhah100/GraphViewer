package classes;

// Static class for math utility
public final class MathUtility {

    private MathUtility() { }

    public static int Sign(int x)
    {
        return Math.abs(x) / x;
    }

    // Return 1 if positive, else negative
    public static int Sign(double x)
    {
        return Sign((int) x);
    }

    // Validate function
    public static boolean isValidateFunction(String function)
    {
        try {
            Function fx = new Function(function);
            return fx.validateFunction();
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    // Is this valid range
    public static boolean isValidateIntegralRange(String start, String end)
    {
        return start != null && end != null && start.length() != 0 && end.length() != 0 && isNumeric(start) && isNumeric(end) && (Double.parseDouble(start) < Double.parseDouble(end));
    }

    // Is the string numeric
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }


}

