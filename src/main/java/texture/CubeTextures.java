package texture;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.JsonFileLoader;

import java.util.*;

public class CubeTextures{

    public static final Integer COMBINED_TEXTURE_TOTAL_WIDTH = 112;
    public static final Integer COMBINED_TEXTURE_TOTAL_HEIGHT = 16;

    private static final List<int[]> cubeTextures = new ArrayList<>();
    private static final Map<Integer, TexturePosition> texturesPositions = new HashMap<>();

    private static final String TEXTURES_MAPPINGS_FILE_PATH = "textures/textures-mappings.json";

    static {

        TypeReference<List<CubeTexturesInfo>> typeReference = new TypeReference<>(){};

        List<CubeTexturesInfo> allCubeTexturesInfos = JsonFileLoader.loadList(TEXTURES_MAPPINGS_FILE_PATH, typeReference);

        for (CubeTexturesInfo cubeTexturesInfo : allCubeTexturesInfos) {

            List<CubeTexture> inputCubeTextures = cubeTexturesInfo.textures();
            CubeTexture defaultCubeTexture = cubeTexturesInfo.defaultTexture();

            Integer defaultTextureIndex = loadTexture(defaultCubeTexture);

            int[] cubeTexturesIds = loadTextures(inputCubeTextures, defaultTextureIndex);

            cubeTextures.add(cubeTexturesIds);
        }
    }

    private static int[] loadTextures(List<CubeTexture> inputCubeTextures, int defaultTextureIndex) throws IllegalStateException{

        int[] cubeTexturesIndexes = new int[6];

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

        int[] cubeTexturesIds = new int[6];

        for(int i = 0; i < 6; i++){

            cubeTexturesIds[i] = defaultTextureIndex;
        }

        return cubeTexturesIds;
    }

    private static int loadTexture(CubeTexture cubeTexture){

        if(cubeTexture == null){
            return -1;
        }

        TexturePosition texturePosition = new TexturePosition(cubeTexture.getRow(), cubeTexture.getCol());

        texturesPositions.put(Byte.valueOf(cubeTexture.getCol()).intValue(), texturePosition);

        return cubeTexture.getCol();
    }

    public static TexturePosition getTexturePosition(int textureIndex){

        return texturesPositions.get(textureIndex);
    }

    public static int[] getCubeTextures(byte textureOrder){

        return cubeTextures.get(textureOrder);
    }

}
