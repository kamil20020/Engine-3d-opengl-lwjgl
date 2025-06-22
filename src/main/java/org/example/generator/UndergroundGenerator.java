package org.example.generator;

import org.example.Chunk;
import texture.CubeTextures;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UndergroundGenerator extends Generator{

    private static final float UNDERGROUND_HEIGHT_NOISE_GENERATOR_SCALE = 0.01f;
    private static final float UNDERGROUND_NOISE_GENERATOR_SCALE = 0.05f;

    private static final int UNDERGROUND_HEIGHT = Chunk.CHUNKS_HEIGHT - 15;

    private static final List<Map.Entry<String, Float>> cubeTexturesProbabilitiesMappings;

    static {

        cubeTexturesProbabilitiesMappings = List.of(
            new AbstractMap.SimpleEntry<>("air", 0.2f),
            new AbstractMap.SimpleEntry<>("dirt", 0.05f),
            new AbstractMap.SimpleEntry<>("gravel", 0.05f),
            new AbstractMap.SimpleEntry<>("granite", 0.05f),
            new AbstractMap.SimpleEntry<>("stone", 1.1f)
        );
    }

    public UndergroundGenerator(long seed) {

        super(seed, UNDERGROUND_HEIGHT_NOISE_GENERATOR_SCALE, UNDERGROUND_NOISE_GENERATOR_SCALE, cubeTexturesProbabilitiesMappings);
    }

    @Override
    public void prepareForChunk(int chunkX, int chunkZ) {

        super.prepareForChunk(chunkX, chunkZ, UNDERGROUND_HEIGHT);
    }

    @Override
    public int generate(byte[][][] chunk, int x, int z, int minY) {

        return super.generate(chunk, x, z, UNDERGROUND_HEIGHT, minY);
    }

}
