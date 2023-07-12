package com.bosspvp.api.utils;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import redempt.crunch.Crunch;

import java.util.concurrent.ThreadLocalRandom;

/**
 * MathUtils
 * <p></p>
 * Contains fast trigonometry methods
 * and other
 */
public class MathUtils {
    /**
     * Sin lookup table.
     */
    private static final double[] SIN_LOOKUP = new double[65536];

    static public final double degreesToRadians = Math.PI / 180;

    static {

        for (int i = 0; i < 65536; ++i) {
            SIN_LOOKUP[i] = Math.sin((double) i * 3.141592653589793D * 2.0D / 65536.0D);
        }
    }


    /**
     * Get sin from lookup table
     * it is significantly faster that Math#sin()
     *
     * @param radians the radians
     * @return result
     */
    public static double fastSin(final double radians) {
        float f = (float) radians;
        return SIN_LOOKUP[(int) (f * 10430.378F) & '\uffff'];
    }

    /**
     * Get cos from lookup table
     * it is significantly faster that Math#cos()
     *
     * @param radians the radians
     * @return result
     */
    public static double fastCos(final double radians) {
        float f = (float) radians;
        return SIN_LOOKUP[(int) (f * 10430.378F + 16384.0F) & '\uffff'];
    }

    /**
     * Get tan from lookup table
     * it is significantly faster that Math#tan()
     *
     * @param radians the radians
     * @return result
     */
    public static double fastTan(final double radians) {
        float f = (float) radians;
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

    /**
     * Get random int inside bounds.
     * Uses ThreadLocalRandom
     *
     * @param min The min bound
     * @param max The max bound
     * @return result
     */
    public static int randInt(final int min,
                              final int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    /**
     * Get random int inside bounds.
     * Uses ThreadLocalRandom
     *
     * @param min The min bound
     * @param max The max bound
     * @return result
     */
    public static double randDouble(final double min,
                                   final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }


    public static double evaluate(String expression){

        return Crunch.compileExpression(expression).evaluate();
    }
    public static Vector vectorFromAngle(double angle){
        Vector vector=new Vector(0,0,0);
        vector.setX(fastCos(angle));
        vector.setY(fastSin(angle));
        return vector;
    }
    public static double angleFromVector(Vector vector){
        return Math.atan2(vector.getY(),vector.getX());
    }
    public static double getMagnitude(Vector vector){
        return Math.sqrt(Math.pow(vector.getX(),2)+Math.pow(vector.getY(),2)+Math.pow(vector.getZ(),2));
    }
    public static Vector setMagnitude(Vector vector, double newMagnitude){
        double currentMagnitude=getMagnitude(vector);
        vector.setX(vector.getX()*newMagnitude/currentMagnitude);
        vector.setY(vector.getY()*newMagnitude/currentMagnitude);
        vector.setZ(vector.getZ()*newMagnitude/currentMagnitude);
        return vector;
    }

    @NotNull
    public static Vector rotateAroundX(@NotNull Vector vector, double angle) {
        double angleCos = fastCos(angle);
        double angleSin = fastSin(angle);

        double y = angleCos * vector.getY() - angleSin * vector.getZ();
        double z = angleSin * vector.getY() + angleCos * vector.getZ();
        return vector.setY(y).setZ(z);
    }


    @NotNull
    public static Vector rotateAroundY(@NotNull Vector vector, double angle) {
        double angleCos = fastCos(angle);
        double angleSin = fastSin(angle);

        double x = angleCos * vector.getX() + angleSin * vector.getZ();
        double z = -angleSin * vector.getX() + angleCos * vector.getZ();
        return vector.setX(x).setZ(z);
    }

    @NotNull
    public static Vector rotateAroundZ(@NotNull Vector vector, double angle) {
        double angleCos = fastCos(angle);
        double angleSin = fastSin(angle);

        double x = angleCos * vector.getX() - angleSin * vector.getY();
        double y = angleSin * vector.getX() + angleCos * vector.getY();
        return vector.setX(x).setY(y);
    }
    private MathUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }
}
