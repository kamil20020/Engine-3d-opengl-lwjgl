package texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public interface Texture {

    public static int createTexture(String textureFileUrl){

        BufferedImage bufferedImage = loadImage(textureFileUrl);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);

        for (int y = height - 1; y >= 0; y--) {

            for (int x = 0; x < width; x++) {

                int pixel = bufferedImage.getRGB(x, y); // ARGB

                int alpha = (pixel >> 24) & 0xFF;
                int red   = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8)  & 0xFF;
                int blue  = pixel & 0xFF;

                buffer.put((byte) red);
                buffer.put((byte) green);
                buffer.put((byte) blue);
                buffer.put((byte) alpha);
            }
        }

        buffer.flip();

        //generate a texture handle or unique ID for this texture
        int textureId = glGenTextures();

        //bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        //use an alignment of 1 to be safe
        //this tells OpenGL how to unpack the RGBA bytes we will specify
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //set up our texture parameters
        //Setup filtering, i.e. how OpenGL will interpolate the pixels when scaling up or down
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //Setup wrap mode, i.e. how OpenGL will handle pixels outside of the expected range
        //Note: GL_CLAMP_TO_EDGE is part of GL12
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        //upload our ByteBuffer to GL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        return textureId;
    }

    private static BufferedImage loadImage(String filePath){

        URL gotResourceUrl = Texture.class.getClassLoader().getResource(filePath);

        BufferedImage bufferedImage = null;

        try {
            File file = new File(gotResourceUrl.toURI());

            bufferedImage = ImageIO.read(file);
        }
        catch (URISyntaxException | IOException e) {

            e.printStackTrace();
        }

        return bufferedImage;
    }

    public static void useTexture(int textureId){

        //bind the texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
    }
}
