package org.example.generator;

import org.example.Chunk;
import texture.CubeTextures;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class TerrainGenerator extends Generator{

    private static final float TERRAIN_HEIGHT_NOISE_GENERATOR_SCALE = 0.01f;
    private static final float TERRAIN_NOISE_GENERATOR_SCALE = 0.05f;

    private static final int TERRAIN_HEIGHT = 10;

    private static final List<Map.Entry<String, Float>> cubeTexturesProbabilitiesMappings;

    static {

        cubeTexturesProbabilitiesMappings = List.of(
            new AbstractMap.SimpleEntry<>("air", 0.05f),
            new AbstractMap.SimpleEntry<>("sand", 0.15f),
            new AbstractMap.SimpleEntry<>("gravel", 0.1f),
            new AbstractMap.SimpleEntry<>("dirt", 1.1f)
        );
    }

    public TerrainGenerator(long seed) {

        super(seed, TERRAIN_HEIGHT_NOISE_GENERATOR_SCALE, TERRAIN_NOISE_GENERATOR_SCALE, cubeTexturesProbabilitiesMappings);
    }

    @Override
    public void prepareForChunk(int chunkX, int chunkZ) {

        super.prepareForChunk(chunkX, chunkZ, TERRAIN_HEIGHT);
    }

    @Override
    public int generate(byte[][][] chunk, int x, int z, int minY) {

        int maxTerrainHeight = super.generate(chunk, x, z, TERRAIN_HEIGHT - 1, minY);

        if(maxTerrainHeight <= 0){

            return maxTerrainHeight;
        }

        chunk[minY + maxTerrainHeight - 1][x][z] = CubeTextures.getCubeTextureIndex("grass");

        return maxTerrainHeight;
    }

}
