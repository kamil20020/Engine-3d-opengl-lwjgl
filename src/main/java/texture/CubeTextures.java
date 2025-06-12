package texture;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.JsonFileLoader;

import java.util.*;

public class CubeTextures{

    private static int textureIndex = 0;

    public static final Integer COMBINED_TEXTURE_TOTAL_WIDTH = 96;
    public static final Integer COMBINED_TEXTURE_TOTAL_HEIGHT = 16;

    private static final Map<Byte, int[]> cubeTexturesMappings = new HashMap<>();
    private static final Map<Integer, TexturePosition> texturesPositions = new HashMap<>();
    private static final List<String> cubeTexturesOrdered = new LinkedList<>();
    private static final Map<String, Integer> loadedTexturesIndexes = new HashMap<>();

    private static final String TEXTURES_MAPPINGS_FILE_PATH = "textures/textures-mappings.json";

    static {

        TypeReference<List<CubeTexturesInfo>> typeReference = new TypeReference<>(){};

        List<CubeTexturesInfo> allCubeTexturesInfos = JsonFileLoader.loadList(TEXTURES_MAPPINGS_FILE_PATH, typeReference);

        for(byte cubeTexturesI = 0; cubeTexturesI < allCubeTexturesInfos.size(); cubeTexturesI++){

            CubeTexturesInfo cubeTexturesInfo = allCubeTexturesInfos.get(cubeTexturesI);

            List<CubeTexture> inputCubeTextures = cubeTexturesInfo.textures();
            CubeTexture defaultCubeTexture = cubeTexturesInfo.defaultTexture();

            Integer defaultTextureIndex = loadTexture(defaultCubeTexture);

            int[] cubeTexturesIds = loadTextures(inputCubeTextures, defaultTextureIndex);

            cubeTexturesMappings.put(cubeTexturesI, cubeTexturesIds);
        }

        System.out.println();
    }

    private static int[] loadTextures(List<CubeTexture> inputCubeTextures, int defaultTextureIndex) throws IllegalStateException{

        int[] cubeTexturesIndexes = new int[8];

        if(inputCubeTextures == null || inputCubeTextures.isEmpty()){

            return loadTexturesAsDefault(defaultTextureIndex);
        }

        for(int i = 0; i < 6; i++){

            CubeTexture cubeTexture = inputCubeTextures.get(i);

            int gotTextureId;

            if(cubeTexture == null){

                gotTextureId = defaultTextureIndex;
            }
            else{

                gotTextureId = loadTexture(cubeTexture);
            }

            if(gotTextureId == -1){

                throw new IllegalStateException("Could not load texture " + cubeTexture);
            }

            cubeTexturesIndexes[i] = gotTextureId;
        }

        return cubeTexturesIndexes;
    }

    private static int[] loadTexturesAsDefault(int defaultTextureIndex){

        int[] cubeTexturesIds = new int[8];

        for(int i = 0; i < 6; i++){

            cubeTexturesIds[i] = defaultTextureIndex;
        }

        return cubeTexturesIds;
    }

    private static int loadTexture(CubeTexture cubeTexture){

        if(cubeTexture == null){
            return -1;
        }

        String textureName = cubeTexture.getName();

        if(loadedTexturesIndexes.containsKey(textureName)){

            return loadedTexturesIndexes.get(textureName);
        }

        textureIndex++;

        TexturePosition texturePosition = new TexturePosition(cubeTexture.getRow(), cubeTexture.getCol());

        texturesPositions.put(textureIndex, texturePosition);

        loadedTexturesIndexes.put(textureName, textureIndex);

        cubeTexturesOrdered.add(textureName);

        return textureIndex;
    }

    public static int getTextureIndex(String textureName){

        return loadedTexturesIndexes.get(textureName);
    }

    public static TexturePosition getTexturePosition(int textureIndex){

        return texturesPositions.get(textureIndex);
    }

    public static int[] getCubeTextures(byte textureOrder){

        return cubeTexturesMappings.get(textureOrder);
    }

    public static String getTextureName(byte textureOrder){

        return cubeTexturesOrdered.get(textureOrder);
    }

}
