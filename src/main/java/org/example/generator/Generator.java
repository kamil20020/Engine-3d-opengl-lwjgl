package org.example.generator;

import org.example.Chunk;
import org.example.math.PerlinNoise2d;
import org.example.math.PerlinNoise3d;
import texture.CubeTextures;

import java.util.List;
import java.util.Map;

public abstract class Generator {

    private double[][] terrainHeightNoise;
    private double[][][] terrainNoise;

    private final PerlinNoise2d terrainHeightNoiseGenerator;
    private final PerlinNoise3d terrainNoiseGenerator;

    private final List<Map.Entry<String, Float>> cubeTexturesProbabilitiesMappings;

    public Generator(long seed, float terrainHeightNoiseGeneratorScale, float terrainNoiseGeneratorScale, List<Map.Entry<String, Float>> cubeTexturesProbabilitiesMappings){

        terrainHeightNoiseGenerator = new PerlinNoise2d(seed, terrainHeightNoiseGeneratorScale);
        terrainNoiseGenerator = new PerlinNoise3d(seed, terrainNoiseGeneratorScale);
        this.cubeTexturesProbabilitiesMappings = cubeTexturesProbabilitiesMappings;
    }

    public abstract void prepareForChunk(int chunkX, int chunkZ);

    protected void prepareForChunk(int chunkX, int chunkZ, int terrainHeight){

        terrainHeightNoise = terrainHeightNoiseGenerator.perlin(chunkX, 16, chunkZ, 16);
        terrainNoise = terrainNoiseGenerator.perlin(chunkX, 16,terrainHeight, terrainHeight, chunkZ, 16);
    }

    public abstract int generate(byte[][][] chunk, int x, int z, int minY);

    protected int generate(byte[][][] chunk, int x, int z, int terrainHeight, int minY){

        int maxTerrainHeight = (int) (terrainHeightNoise[x][z] * terrainHeight) % terrainHeight;

        if(maxTerrainHeight <= 0){

            return maxTerrainHeight;
        }

        for(int y = 0; y < maxTerrainHeight; y++){

            double terrainNoiseValue = terrainNoise[y][x][z];

            byte blockType = getBlockType(terrainNoiseValue);

            chunk[minY + y][x][z] = blockType;
        }

        return maxTerrainHeight;
    }

    private byte getBlockType(double noiseValue){

        String blockType = null;

        double sumProbability = 0f;

        for(Map.Entry<String, Float> cubeTextureProbabilityMapping : cubeTexturesProbabilitiesMappings){

            float probability = cubeTextureProbabilityMapping.getValue();

            sumProbability += probability;

            if(noiseValue < sumProbability){

                blockType = cubeTextureProbabilityMapping.getKey();

                break;
            }
        }

        return CubeTextures.getCubeTextureIndex(blockType);
    }

}
