package classes;

public final class MathUtility {

    private MathUtility() { }

    public static int Sign(int x)
    {
        return Math.abs(x) / x;
    }

    public static int Sign(double x)
    {
        return Sign((int) x);
    }

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


}

