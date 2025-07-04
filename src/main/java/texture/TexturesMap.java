package texture;

import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.core.type.TypeReference;
import org.example.JsonFileLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TexturesMap {

    private static final Map<String, TexturePosition> texturesPositionsMappings = new HashMap<>();

    private static final String TEXTURES_FILE_PATH = "textures/textures.json";

    static {

        TypeReference<List<MapTexture>> typeReference = new TypeReference<>(){};

        List<MapTexture> mapTextures = JsonFileLoader.loadList(TEXTURES_FILE_PATH, typeReference);

        for (MapTexture mapTexture : mapTextures) {

            String textureId = mapTexture.getId();

            byte textureRow = mapTexture.getRow();
            byte textureCol = mapTexture.getCol();

            TexturePosition texturePosition = new TexturePosition(textureRow, textureCol);

            texturesPositionsMappings.put(textureId, texturePosition);
        }
    }

    public static TexturePosition getTexturePosition(String textureName){

        return texturesPositionsMappings.get(textureName);
    }

}
