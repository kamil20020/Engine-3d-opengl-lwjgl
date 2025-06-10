package org.example;

import org.example.math.PerlinNoise2d;
import org.example.math.PerlinNoise3d;

public class Generator {

    private final PerlinNoise2d heightNoiseGenerator;
    private final PerlinNoise3d terrainNoiseGenerator;

    public Generator(long seed){

        heightNoiseGenerator = new PerlinNoise2d(seed, 0.05f);
        terrainNoiseGenerator = new PerlinNoise3d(seed, 0.08f);
    }

    public byte[][][] initChunk(){

        byte[][][] chunk = new byte[384][16][16];

        double[][] heightsNoise = heightNoiseGenerator.perlin(16, 16);
        double[][][] terrainNoise = terrainNoiseGenerator.perlin(16, 384, 16);

        for(int x = 0; x < 16; x++){

            for(int z = 0; z < 16; z++){

                double maxHeight = (heightsNoise[x][z] * 384) % 384;

                for(int y = 0; y < maxHeight; y++){

                    double terrainNoiseValue = terrainNoise[y][x][z];

                    byte blockType = getBlockType(terrainNoiseValue);

                    chunk[y][x][z] = blockType;
                }
            }
        }

        return chunk;
    }

    private byte getBlockType(double terrainNoiseValue){

        byte blockType = 0;

        if(terrainNoiseValue < 0.2d){

            blockType = 0;
        }
        else if(terrainNoiseValue < 0.4d){

            blockType = 1;
        }
        else if(terrainNoiseValue < 0.6d){

            blockType = 2;
        }
        else if(terrainNoiseValue < 0.8d){

            blockType = 3;
        }
        else{
            blockType = 4;
        }

        return blockType;
    }
}
