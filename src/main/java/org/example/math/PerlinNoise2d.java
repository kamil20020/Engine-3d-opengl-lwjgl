package org.example.math;

import java.util.Random;

public class PerlinNoise2d {

    private Random random;

    private final int[] permutation = new int[512];

    private static final int[][] gradients;

    private final float scale;

    private static final long DEFAULT_SEED = 123;

    static {

        gradients = new int[][]{
            { 1, 1}, {-1, 1}, { 1,-1}, {-1,-1},
            { 1, 0}, {-1, 0}, { 0, 1}, { 0,-1}
        };
    }

    public PerlinNoise2d(long seed, float scale){

        this.scale = scale;
        this.random = new Random(seed);

        initPermutation();
    }

    public PerlinNoise2d(boolean shouldGetDefaultSeed, float scale){

        this.scale = scale;

        if(shouldGetDefaultSeed){

            this.random = new Random(DEFAULT_SEED);
        }
        else{

            this.random = new Random();
        }

        initPermutation();
    }

    private void initPermutation(){

        for(int i = 0; i < 256; i++){

            permutation[i] = i;
        }

        for(int i = 0; i < 256; i++){

            int j = i + random.nextInt(256 - i);

            swap(permutation, i, j);
        }

        for(int i = 0; i < 256; i++){

            permutation[256 + i] = permutation[i];
        }
    }

    public double[][] perlin(int minX, int width, int minY, int height){

        double[][] results = new double[height][width];

        for(int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                double x = (double) (j + minX) * scale;
                double y = (double) (i + minY) * scale;

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

        double sx = fade(x - (double) x0);
        double sy = fade(y - (double) y0);

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

        return dotProduct(dx, gradientOnPosition[0], dy, gradientOnPosition[1]);
    }

    private int[] getGradientOnPosition(int x, int y){

        x = ((x % 256) + 256) % 256;
        y = ((y % 256) + 256) % 256;

        int hash = permutation[x + permutation[y]];

        int index = hash % gradients.length;

        return gradients[index];
    }

    private static double lerp(double a0, double a1, double w){

        return (1f - w) * a0 + w * a1;
    }

    private static double fade(double t) {

        return t * t * t * (t * (t * 6 - 15) + 10);
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

        PerlinNoise2d perlinNoise = new PerlinNoise2d(true, 0.05f);

        double[][] perlinValues = perlinNoise.perlin(0, 800, 0, 800);

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
