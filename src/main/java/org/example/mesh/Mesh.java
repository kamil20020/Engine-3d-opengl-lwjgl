package org.example.mesh;

import org.joml.Vector2f;
import org.joml.Vector3f;
import texture.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Mesh {

    private final Vector3f[] vertices;
    private final Integer[] triangles;
    private final Vector2f[] textureCords;
    private final Integer[] textureCordsForVertices;

    public Mesh(Vector3f[] vertices, Integer[] triangles, Vector2f[] textureCords,  Integer[] textureCordsForVertices){

        this.vertices = vertices;
        this.triangles = triangles;
        this.textureCords = textureCords;
        this.textureCordsForVertices = textureCordsForVertices;
    }

    public void draw(int[] textures, float xMin, float yMin, float zMin){

        for(int i = 0, textureIndex = 0; i < triangles.length; i += 4, textureIndex++){

            int texture = textures[textureIndex];

            Texture.useTexture(texture);

            glBegin(GL_QUADS);

            for(int j = 0; j < 4; j++){

                int vIndex = triangles[i + j];

                Vector3f v = vertices[vIndex];

                int vTextureCordsForVertexIndex = textureCordsForVertices[i + j];
                Vector2f textureCordsV = textureCords[vTextureCordsForVertexIndex];

                glTexCoord2f(textureCordsV.x, textureCordsV.y);
                glVertex3f(xMin + v.x, yMin + v.y, zMin + v.z);
            }

            glEnd();
        }
    }
}
