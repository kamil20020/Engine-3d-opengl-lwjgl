package org.example.math;

import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.Arrays;
import java.util.Random;

public class PerlinNoise {

    private static final double I_X_MAX = 100;
    private static final double I_Y_MAX = 100;

    private int permutation[] = new int[512];

    private final Random random;

    private static final int[][] gradients;

    private static final float SCALE = 0.05f;

    static {

        gradients = new int[][]{
            { 1, 1}, {-1, 1}, { 1,-1}, {-1,-1},
            { 1, 0}, {-1, 0}, { 0, 1}, { 0,-1}
        };
    }

    public PerlinNoise(long seed){

        this.random = new Random(seed);

        initPermutation();
    }

    public PerlinNoise(){

        this.random = new Random();

        initPermutation();
    }

    private void initPermutation(){

        for(int i = 0; i < 256; i++){

            permutation[i] = i;
        }

        for(int i = 0; i < 256; i++){

            int j = random.nextInt(256 - i);

            swap(permutation, i, j);
        }

        for(int i = 0; i < 256; i++){

            permutation[256 + i] = permutation[i];
        }
    }

    public double[][] perlin(int width, int height){

        double[][] results = new double[height][width];

        for(int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                double x = (double) j * SCALE;
                double y = (double) i * SCALE;

                double perlinValue = perlin(x, y);
                double normalizedValue = (perlinValue + 1d) / 2d;

                results[i][j] = normalizedValue;
            }
        }

        return results;
    }

    private double perlin(double x, double y){

        int x0 = (int) Math.floor(x);
        int x1 = x0 + 1;
        int y0 = (int) Math.floor(y);
        int y1 = y0 + 1;

        double sx = x - (double) x0;
        double sy = y - (double) y0;

        double n0 = dotProductGridGradient(x0, y0, x, y);
        double n1 = dotProductGridGradient(x1, y0, x, y);
        double ix0  = lerp(n0, n1, sx);

        n0 = dotProductGridGradient(x0, y1, x, y);
        n1 = dotProductGridGradient(x1, y1, x, y);
        double ix1 = lerp(n0, n1, sx);

        return lerp(ix0, ix1, sy);
    }

    private double dotProductGridGradient(int ix, int iy, double x, double y){

        double dx = x - (double) ix;
        double dy = y - (double) iy;

        int[] gradientOnPosition = getGradientOnPosition(ix, iy);

        return dx * gradientOnPosition[0] + dy * gradientOnPosition[1];
    }

    private int[] getGradientOnPosition(int x, int y){

        int hash = permutation[x + permutation[y]];

        int index = hash % 8;

        return gradients[index];
    }

    private static double lerp(double a0, double a1, double w){

        return (1f - w) * a0 + w * a1;
    }

    private static double dotProduct(double ax, double ay, double bx, double by){

        return ax * bx + ay * by;
    }

    private static void swap(int[] tab, int index1, int index2){

        int buffer = tab[index1];

        tab[index1] = tab[index2];
        tab[index2] = buffer;
    }

    public static void main(String[] args){

        PerlinNoise perlinNoise = new PerlinNoise();

        double[][] perlinValues = perlinNoise.perlin(800, 800);

        for(int i = 0; i < 100; i++){

            for(int j = 0; j < 100; j++){

                double perlinValue = perlinValues[i][j];

                char c = ' ';

                if(perlinValue <= 0.3d){

                    c = 's';
                }
                else if(perlinValue <= 0.7d){

                    c = 'p';
                }
                else{

                    c = 'g';
                }

                System.out.print(c);
            }

            System.out.println();
        }
    }

}
