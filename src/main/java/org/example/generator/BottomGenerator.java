package org.example.generator;

import org.example.Chunk;
import texture.CubeTextures;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class BottomGenerator extends Generator{

    private static final int BOTTOM_HEIGHT = 5;

    private static final float BOTTOM_HEIGHT_NOISE_GENERATOR_SCALE = 0.01f;
    private static final float BOTTOM_NOISE_GENERATOR_SCALE = 0.05f;

    private static final List<Map.Entry<String, Float>> cubeTexturesProbabilitiesMappings;

    static {

        cubeTexturesProbabilitiesMappings = List.of(
            new AbstractMap.SimpleEntry<>("bedrock", 1.1f)
        );
    }

    public BottomGenerator(long seed) {

        super(seed, BOTTOM_HEIGHT_NOISE_GENERATOR_SCALE, BOTTOM_NOISE_GENERATOR_SCALE, cubeTexturesProbabilitiesMappings);
    }

    @Override
    public void prepareForChunk(int chunkX, int chunkZ) {

        super.prepareForChunk(chunkX, chunkZ, BOTTOM_HEIGHT);
    }

    @Override
    public int generate(byte[][][] chunk, int x, int z, int minY) {

        chunk[minY][x][z] = CubeTextures.getCubeTextureIndex("bedrock");

        return super.generate(chunk, x, z, BOTTOM_HEIGHT - 1, minY + 1) + 1;
    }

}
