package texture;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.JsonFileLoader;

import java.util.*;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

public class CubeTextures{

    private static final Map<String, int[]> cubeTexturesMappings = new HashMap<>();
    private static final List<String> cubeTexturesOrdered = new LinkedList<>();
    private static final Map<String, Integer> loadedTextures = new HashMap<>();

    private static final String TEXTURES_MAPPINGS_FILE_PATH = "textures/textures-mappings.json";

    static {

        TypeReference<List<CubeTexturesInfo>> typeReference = new TypeReference<>(){};

        List<CubeTexturesInfo> allCubeTexturesInfos = JsonFileLoader.loadList(TEXTURES_MAPPINGS_FILE_PATH, typeReference);

        for(CubeTexturesInfo cubeTexturesInfo : allCubeTexturesInfos){

            String id = cubeTexturesInfo.id();

            List<String> texturesNames = cubeTexturesInfo.texturesNames();
            String defaultTextureName = cubeTexturesInfo.defaultTextureName();

            Integer defaultTexture = loadTexture(defaultTextureName);

            int[] cubeTextures = loadTextures(texturesNames, defaultTexture);

            cubeTexturesMappings.put(id, cubeTextures);

            cubeTexturesOrdered.add(id);
        }
    }

    private static int[] loadTextures(List<String> texturesNames, int defaultTexture){

        int[] textures = new int[8];

        if(texturesNames == null || texturesNames.isEmpty()){

            return loadTexturesAsDefault(defaultTexture);
        }

        for(int i = 0; i < 6; i++){

            String textureName = texturesNames.get(i);

            int actualTexture;

            if(textureName == null){

                actualTexture = defaultTexture;
            }
            else{

                actualTexture = loadTexture(textureName);
            }

            textures[i] = actualTexture;
        }

        return textures;
    }

    private static int[] loadTexturesAsDefault(int defaultTexture){

        int[] textures = new int[8];

        for(int i = 0; i < 6; i++){

            textures[i] = defaultTexture;
        }

        return textures;
    }

    private static int loadTexture(String textureName){

        if(textureName == null){
            return -1;
        }

        if(loadedTextures.containsKey(textureName)){

            return loadedTextures.get(textureName);
        }

        int loadedTexture = Texture.createTexture(textureName);

        loadedTextures.put(textureName, loadedTexture);

        return loadedTexture;
    }

    public static void clear(){

        int[] loadedTexturesValues = loadedTextures.values().stream()
            .mapToInt(texture -> texture)
            .toArray();

        glDeleteTextures(loadedTexturesValues);

        loadedTextures.clear();
    }

    public static int[] getCubeTextures(String id){

        return cubeTexturesMappings.get(id);
    }

    public static String getTextureName(byte textureOrder){

        return cubeTexturesOrdered.get(textureOrder);
    }

}
