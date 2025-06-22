package org.example.generator;

import org.example.Chunk;

import java.util.List;

public class CombinedGenerator {

    private final List<Generator> generators;

    public CombinedGenerator(long seed){

        generators = List.of(
            new BottomGenerator(seed),
            new UndergroundGenerator(seed),
            new TerrainGenerator(seed)
        );
    }

    public byte[][][] initChunk(int chunkX, int chunkZ){

        byte[][][] chunk = new byte[Chunk.CHUNKS_HEIGHT][16][16];

        for(Generator generator : generators) {

            generator.prepareForChunk(chunkX, chunkZ);
        }

        for(int x = 0; x < 16; x++){

            for(int z = 0; z < 16; z++){

                int minY = 0;

                for(Generator generator : generators){

                    minY += generator.generate(chunk, x, z, minY);
                }
            }
        }

        return chunk;
    }

}
