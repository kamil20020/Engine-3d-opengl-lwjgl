package org.example.math;

import java.util.Random;

public class PerlinNoise3d {

    private final Random random;

    private final int[] permutation = new int[512];

    private static final int[][] gradients;

    private static final float SCALE = 0.05f;
    private static final long DEFAULT_SEED = 123;

    static {

        gradients = new int[][]{
            { 1, 1, 0}, {-1, 1, 0}, { 1,-1, 0}, {-1,-1, 0},
            { 1, 0, 1}, {-1, 0, 1}, { 1, 0,-1}, {-1, 0,-1},
            { 0, 1, 1}, { 0,-1, 1}, { 0, 1,-1}, { 0,-1,-1}
        };
    }

    public PerlinNoise3d(long seed){

        this.random = new Random(seed);

        initPermutation();
    }

    public PerlinNoise3d(boolean shouldGetDefaultSeed){

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

            int j = random.nextInt(256 - i);

            swap(permutation, i, j);
        }

        for(int i = 0; i < 256; i++){

            permutation[256 + i] = permutation[i];
        }
    }

    public double[][][] perlin(int width, int height, int depth){

        double[][][] results = new double[height][width][depth];

        for(int i = 0; i < height; i++){

            for(int j = 0; j < width; j++) {

                for (int k = 0; k < depth; k++) {

                    double x = (double) j * SCALE;
                    double y = (double) i * SCALE;
                    double z = (double) k * SCALE;

                    double perlinValue = perlin(x, y, z);
                    double normalizedValue = (perlinValue + 1d) / 2d;

                    results[i][j][k] = normalizedValue;
                }
            }
        }

        return results;
    }

    private double perlin(double x, double y, double z){

        int x0 = (int) Math.floor(x);
        int x1 = x0 + 1;
        int y0 = (int) Math.floor(y);
        int y1 = y0 + 1;
        int z0 = (int) Math.floor(z);
        int z1 = z0 + 1;

        double sx = x - (double) x0;
        double sy = y - (double) y0;
        double sz = z - (double) z0;

        double n0 = dotProductGridGradient(x0, y0, z0, x, y, z);
        double n1 = dotProductGridGradient(x1, y0, z0, x, y, z);
        double ix0  = lerp(n0, n1, sx);

        n0 = dotProductGridGradient(x0, y1, z0, x, y, z);
        n1 = dotProductGridGradient(x1, y1, z0, x, y, z);
        double ix1 = lerp(n0, n1, sx);

        n0 = dotProductGridGradient(x0, y0, z1, x, y, z);
        n1 = dotProductGridGradient(x1, y0, z1, x, y, z);
        double ix2 = lerp(n0, n1, sx);

        n0 = dotProductGridGradient(x0, y1, z1, x, y, z);
        n1 = dotProductGridGradient(x1, y1, z1, x, y, z);
        double ix3 = lerp(n0, n1, sx);

        double iy0 = lerp(ix0, ix1, sy);
        double iy1 = lerp(ix2, ix3, sy);

        return lerp(iy0, iy1, sz);
    }

    private double dotProductGridGradient(int ix, int iy, int iz, double x, double y, double z){

        double dx = x - (double) ix;
        double dy = y - (double) iy;
        double dz = z - (double) iz;

        int[] gradientOnPosition = getGradientOnPosition(ix, iy, iz);

        return dotProduct(dx, gradientOnPosition[0], dy, gradientOnPosition[1], dz, gradientOnPosition[2]);
    }

    private int[] getGradientOnPosition(int x, int y, int z){

        int hash = permutation[z + permutation[x + permutation[y]]];

        int index = hash % gradients.length;

        return gradients[index];
    }

    private static double lerp(double a0, double a1, double w){

        return (1f - w) * a0 + w * a1;
    }

    private static double dotProduct(double ax, double ay, double bx, double by, double cx, double cy){

        return ax * bx + ay * by + cx * cy;
    }

    private static void swap(int[] tab, int index1, int index2){

        int buffer = tab[index1];

        tab[index1] = tab[index2];
        tab[index2] = buffer;
    }

    public static void main(String[] args){

        PerlinNoise3d perlinNoise = new PerlinNoise3d(true);

        double[][][] perlinValues = perlinNoise.perlin(800, 800, 800);

        for(int i = 0; i < 100; i++){

            for(int j = 0; j < 100; j++){

                double perlinValue = perlinValues[i][j][0];

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
