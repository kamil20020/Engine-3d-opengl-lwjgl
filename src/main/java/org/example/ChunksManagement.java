package org.example;

import org.example.generator.CombinedGenerator;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class ChunksManagement {

    private Chunk[][] chunks;

    private final CombinedGenerator combinedGenerator;

    private final int numberOfChunks;
    private final int chunksWidth;

    public ChunksManagement(int numberOfChunks, CombinedGenerator combinedGenerator) {

        this.numberOfChunks = numberOfChunks;
        this.chunksWidth = numberOfChunks * 2 + 1;
        this.chunks = new Chunk[chunksWidth][chunksWidth];

        this.combinedGenerator = combinedGenerator;
    }

    public void init(){

        float minChunksCord = -numberOfChunks * Chunk.CHUNKS_2D_SIZE - Chunk.CHUNKS_2D_SIZE / 2f;

        Vector2f chunkPos = new Vector2f(minChunksCord, minChunksCord);

        for(int xI = 0; xI < chunksWidth; xI++){

            chunkPos.y = minChunksCord;

            for(int zI = 0; zI < chunksWidth; zI++){

                Vector2f chunkPosCopy = new Vector2f(chunkPos);

                Chunk chunk = new Chunk(chunkPosCopy, combinedGenerator);

                chunk.init();

                chunks[xI][zI] = chunk;

                chunkPos.y += Chunk.CHUNKS_2D_SIZE;
            }

            chunkPos.x += Chunk.CHUNKS_2D_SIZE;
        }
    }

    public void handleRefreshLessX(Vector3f downMaxPos){

        Chunk[][] newChunks = cloneChunks();

        for(int zI = 0; zI < chunksWidth; zI++){

            Chunk oldChunk = chunks[chunksWidth - 1][zI];

            oldChunk.clear();
        }

        for (int xI = 1; xI < chunksWidth; xI++){

            for (int zI = 0; zI < chunksWidth; zI++){

                newChunks[xI][zI] = chunks[xI - 1][zI];
            }
        }

        Vector2f chunkPos = new Vector2f(downMaxPos.x, downMaxPos.z);

        for(int zI = 0; zI < chunksWidth; zI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            newChunks[0][zI] = chunk;

            chunkPos.y += Chunk.CHUNKS_2D_SIZE;
        }

        chunks = newChunks;
    }

    public void handleRefreshMoreX(Vector3f downMaxPos, Vector3f topMaxPos){

        Chunk[][] newChunks = cloneChunks();

        for(int zI = 0; zI < chunksWidth; zI++){

            Chunk oldChunk = chunks[0][zI];

            oldChunk.clear();
        }

        for (int xI = 0; xI < chunksWidth - 1; xI++){

            for(int zI = 0; zI < chunksWidth; zI++){

                newChunks[xI][zI] = chunks[xI + 1][zI];
            }
        }

        Vector2f chunkPos = new Vector2f(topMaxPos.x, downMaxPos.z);

        for(int zI = 0; zI < chunksWidth; zI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            newChunks[chunksWidth - 1][zI] = chunk;

            chunkPos.y += Chunk.CHUNKS_2D_SIZE;
        }

        chunks = newChunks;
    }

    public void handleRefreshLessZ(Vector3f downMaxPos){

        Chunk[][] newChunks = cloneChunks();

        for(int xI = 0; xI < chunksWidth; xI++){

            Chunk oldChunk = chunks[xI][chunksWidth - 1];

            oldChunk.clear();
        }

        for(int xI = 0; xI < chunksWidth; xI++){

            for (int zI = 1; zI < chunksWidth; zI++){

                newChunks[xI][zI] = chunks[xI][zI - 1];
            }
        }

        Vector2f chunkPos = new Vector2f(downMaxPos.x, downMaxPos.z);

        for(int xI = 0; xI < chunksWidth; xI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            newChunks[xI][0] = chunk;

            chunkPos.x += Chunk.CHUNKS_2D_SIZE;
        }

        chunks = newChunks;
    }

    public void handleRefreshMoreZ(Vector3f downMaxPos, Vector3f topMaxPos){

        Chunk[][] newChunks = cloneChunks();

        for(int xI = 0; xI < chunksWidth; xI++){

            Chunk oldChunk = chunks[xI][0];

            oldChunk.clear();
        }

        for(int xI = 0; xI < chunksWidth; xI++){

            for (int zI = chunksWidth - 2; zI >= 0 ; zI--){

                newChunks[xI][zI] = chunks[xI][zI + 1];
            }
        }

        Vector2f chunkPos = new Vector2f(downMaxPos.x, topMaxPos.z);

        for(int xI = 0; xI < chunksWidth; xI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            newChunks[xI][chunksWidth - 1] = chunk;

            chunkPos.x += Chunk.CHUNKS_2D_SIZE;
        }

        chunks = newChunks;
    }

    public Chunk[][] cloneChunks(){

        Chunk[][] newChunks = new Chunk[chunks.length][chunks[0].length];

        for(int i = 0; i < chunks.length; i++){

            Chunk[] chunksRow = chunks[i];

            Chunk[] newChunksRow = new Chunk[chunksRow.length];

            for(int j = 0; j < chunksRow.length; j++){

                newChunksRow[j] = chunksRow[j];
            }

            newChunks[i] = newChunksRow;
        }

        return newChunks;
    }

    public void draw(){

        for(Chunk[] chunksRow : chunks){

            for(Chunk chunk : chunksRow){

                chunk.draw();
            }
        }
    }

}
