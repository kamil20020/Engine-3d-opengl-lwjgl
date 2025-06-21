package org.example;

import org.example.math.PerlinNoise2d;
import org.example.math.PerlinNoise3d;
import texture.CubeTextures;

public class Generator {

    private final PerlinNoise2d undergroundHeightNoiseGenerator;
    private final PerlinNoise3d undergroundNoiseGenerator;

    private final PerlinNoise2d terrainHeightNoiseGenerator;
    private final PerlinNoise3d terrainNoiseGenerator;

    private static final float UNDERGROUND_NOISE_GENERATOR_SCALE = 0.15f;
    private static final float UNDERGROUND_HEIGHT_NOISE_GENERATOR_SCALE = 0.02f;

    private static final float TERRAIN_NOISE_GENERATOR_SCALE = 0.04f;
    private static final float TERRAIN_HEIGHT_NOISE_GENERATOR_SCALE = 0.02f;

    public Generator(long seed){

        undergroundNoiseGenerator = new PerlinNoise3d(seed, UNDERGROUND_NOISE_GENERATOR_SCALE);
        undergroundHeightNoiseGenerator = new PerlinNoise2d(seed, UNDERGROUND_HEIGHT_NOISE_GENERATOR_SCALE);

        terrainNoiseGenerator = new PerlinNoise3d(seed, TERRAIN_NOISE_GENERATOR_SCALE);
        terrainHeightNoiseGenerator = new PerlinNoise2d(seed, TERRAIN_HEIGHT_NOISE_GENERATOR_SCALE);
    }

    public Generator(){

        undergroundNoiseGenerator = new PerlinNoise3d(false, UNDERGROUND_NOISE_GENERATOR_SCALE);
        undergroundHeightNoiseGenerator = new PerlinNoise2d(false, UNDERGROUND_HEIGHT_NOISE_GENERATOR_SCALE);

        terrainNoiseGenerator = new PerlinNoise3d(false, TERRAIN_NOISE_GENERATOR_SCALE);
        terrainHeightNoiseGenerator = new PerlinNoise2d(false, TERRAIN_HEIGHT_NOISE_GENERATOR_SCALE);
    }

    public byte[][][] initChunk(int chunkX, int chunkZ){

        byte[][][] chunk = new byte[Chunk.CHUNKS_HEIGHT][16][16];

        double[][] undergroundHeightNoise = undergroundHeightNoiseGenerator.perlin(chunkX, 16, chunkZ, 16);
        double[][][] undergroundNoise = undergroundNoiseGenerator.perlin(chunkX, 16, 0, Chunk.CHUNKS_HEIGHT - 5, chunkZ, 16);

        double[][] terrainHeightNoise = terrainHeightNoiseGenerator.perlin(chunkX, 16, chunkZ, 16);
        double[][][] terrainNoise = terrainNoiseGenerator.perlin(chunkX, 16,Chunk.CHUNKS_HEIGHT - 5, 5, chunkZ, 16);

        for(int x = 0; x < 16; x++){

            for(int z = 0; z < 16; z++){

                int maxUndergroundHeight = (int) Math.ceil((undergroundHeightNoise[x][z] * (Chunk.CHUNKS_HEIGHT - 5) % (Chunk.CHUNKS_HEIGHT - 5)));

                for(int y = 0; y < maxUndergroundHeight; y++){

                    double undergroundNoiseValue = undergroundNoise[y][x][z];

                    byte blockType = getBlockTypeForUnderground(undergroundNoiseValue);

                    chunk[y][x][z] = blockType;
                }

                int maxTerrainHeight = (int)(terrainHeightNoise[x][z] * (5) % (5));

                for(int y = 0; y < maxTerrainHeight; y++){

                    double terrainNoiseValue = terrainNoise[y][x][z];

                    byte blockType = getBlockType(terrainNoiseValue);

                    chunk[y + maxUndergroundHeight][x][z] = blockType;
                }

                if(maxUndergroundHeight + maxTerrainHeight - 1 < 0){

                    continue;
                }

                chunk[(int) (maxUndergroundHeight + maxTerrainHeight - 1)][x][z] = 3;
            }
        }

        return chunk;
    }

    private byte getBlockTypeForUnderground(double noiseValue){

        String blockType = null;

        if(noiseValue < 0.2d){

            blockType = "air";
        }
        else if(noiseValue < 0.25d){

            blockType = "dirt";
        }
        else if(noiseValue < 0.30d){

            blockType = "gravel";
        }
        else if(noiseValue < 0.35d){

            blockType = "granite";
        }
        else{
            blockType = "stone";
        }

        return CubeTextures.getCubeTextureIndex(blockType);
    }

    private byte getBlockType(double terrainNoiseValue){

        String blockType = null;

        if(terrainNoiseValue < 0.1d){

            blockType = "air";
        }
        else if(terrainNoiseValue < 0.2d){

            blockType = "sand";
        }
        else if(terrainNoiseValue < 0.3d){

            blockType = "gravel";
        }
        else{
            blockType = "dirt";
        }

        return CubeTextures.getCubeTextureIndex(blockType);
    }
}
