package texture;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.JsonFileLoader;
import org.example.mesh.Mesh;
import org.example.mesh.MeshType;

import java.util.*;

public class CubeTextures{

    private static final Map<String, Byte> cubeTexturesNames = new HashMap<>();
    private static final List<MeshType> cubeTexturesTypes = new ArrayList<>();
    private static final List<String[]> cubeTextures = new ArrayList<>();

    private static final String TEXTURES_MAPPINGS_FILE_PATH = "textures/textures-mappings.json";

    public static void init(){

        TypeReference<List<CubeTexturesInfo>> typeReference = new TypeReference<>(){};

        List<CubeTexturesInfo> allCubeTexturesInfos = JsonFileLoader.loadList(TEXTURES_MAPPINGS_FILE_PATH, typeReference);

        byte cubeTextureIndex = 0;

        for (CubeTexturesInfo cubeTexturesInfo : allCubeTexturesInfos) {

            List<String> inputCubeTextures = cubeTexturesInfo.textures();
            String defaultCubeTexture = cubeTexturesInfo.defaultTexture();

            String[] cubeTexturesIds = loadTextures(inputCubeTextures, defaultCubeTexture);

            cubeTextures.add(cubeTexturesIds);

            cubeTexturesNames.put(cubeTexturesInfo.id(), cubeTextureIndex);

            cubeTexturesTypes.add(cubeTexturesInfo.type());

            cubeTextureIndex++;
        }
    }

    private static String[] loadTextures(List<String> inputCubeTextures, String defaultTexture) throws IllegalStateException{

        String[] cubeTexturesIndexes = new String[6];

        if(inputCubeTextures == null || inputCubeTextures.isEmpty()){

            return loadTexturesAsDefault(defaultTexture);
        }

        for(int i = 0; i < 6; i++){

            String cubeTexture = inputCubeTextures.get(i);

            String gotTextureId;

            if(cubeTexture == null){

                gotTextureId = defaultTexture;
            }
            else{

                gotTextureId = cubeTexture;
            }

            if(gotTextureId == null){

                throw new IllegalStateException("Could not load texture " + gotTextureId);
            }

            cubeTexturesIndexes[i] = gotTextureId;
        }

        return cubeTexturesIndexes;
    }

    private static String[] loadTexturesAsDefault(String texture){

        String[] cubeTexturesIds = new String[6];

        for(int i = 0; i < 6; i++){

            cubeTexturesIds[i] = texture;
        }

        return cubeTexturesIds;
    }

    public static byte getCubeTextureIndex(String cubeTextureName){

        return cubeTexturesNames.get(cubeTextureName);
    }

    public static String[] getCubeTextures(byte cubeTextureIndex){

        return cubeTextures.get(cubeTextureIndex);
    }

    public static String[] getCubeTextures(String cubeTextureName){

        byte cubeTextureIndex = cubeTexturesNames.get(cubeTextureName);

        return cubeTextures.get(cubeTextureIndex);
    }

    public static Mesh getMesh(byte cubeTextureIndex){

        MeshType meshType = cubeTexturesTypes.get(cubeTextureIndex);

        return MeshType.getMesh(meshType);
    }

}
