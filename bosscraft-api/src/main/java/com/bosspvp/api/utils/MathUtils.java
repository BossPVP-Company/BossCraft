package com.bosspvp.api.utils;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
    /**
     * Sin lookup table.
     */
    private static final double[] SIN_LOOKUP = new double[65536];


    static {

        for (int i = 0; i < 65536; ++i) {
            SIN_LOOKUP[i] = Math.sin((double) i * 3.141592653589793D * 2.0D / 65536.0D);
        }
    }
    public static double fastSin(final double a) {
        float f = (float) a;
        return SIN_LOOKUP[(int) (f * 10430.378F) & '\uffff'];
    }

    public static double fastCos(final double a) {
        float f = (float) a;
        return SIN_LOOKUP[(int) (f * 10430.378F + 16384.0F) & '\uffff'];
    }
    public static double fastTan(final double a) {
        float f = (float) a;
        return SIN_LOOKUP[(int) (f * 10430.378F) & '\uffff'] /
                SIN_LOOKUP[(int) (f * 10430.378F + 16384.0F) & '\uffff'];
    }

    /**
     * Bias the input value according to a curve.
     *
     * @param input The input value.
     * @param bias  The bias between -1 and 1, where higher values bias input values to lower output values.
     * @return The biased output.
     */
    public static double bias(final double input,
                              final double bias) {
        double k = Math.pow(1 - bias, 3);

        return (input * k) / (input * k - input + 1);
    }


    public static int randInt(final int min,
                              final int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static double randDouble(final double min,
                                   final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }


    private MathUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }
}
