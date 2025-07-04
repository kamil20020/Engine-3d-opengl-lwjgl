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

    private static final byte COMBINED_TEXTURES_IN_ROW = 2;
    private static final byte COMBINED_TEXTURES_IN_COL = 8;
    public static final int COMBINED_TEXTURE_TOTAL_WIDTH = COMBINED_TEXTURES_IN_COL * 16;
    public static final int COMBINED_TEXTURE_TOTAL_HEIGHT = COMBINED_TEXTURES_IN_ROW * 16;

    private static final String TEXTURES_FILE_PATH = "textures/textures.json";

    public static void init() {

        TypeReference<List<MapTexture>> typeReference = new TypeReference<>(){};

        List<MapTexture> mapTextures = JsonFileLoader.loadList(TEXTURES_FILE_PATH, typeReference);

        for (MapTexture mapTexture : mapTextures) {

            String textureId = mapTexture.getId();

            byte textureRow = (byte) (COMBINED_TEXTURES_IN_ROW - 1 - mapTexture.getRow());
            byte textureCol = mapTexture.getCol();

            TexturePosition texturePosition = new TexturePosition(textureRow, textureCol);

            texturesPositionsMappings.put(textureId, texturePosition);
        }
    }

    public static TexturePosition getTexturePosition(String textureName){

        return texturesPositionsMappings.get(textureName);
    }

}
