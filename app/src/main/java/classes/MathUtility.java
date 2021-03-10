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


}

