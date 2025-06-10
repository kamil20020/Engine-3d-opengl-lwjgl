package texture;

import java.util.List;

public record CubeTexturesInfo(
    String id,
    List<String> texturesNames,
    String defaultTextureName
){

    public CubeTexturesInfo(String id, List<String> texturesNames, String defaultTextureName) {

        this.id = id;
        this.texturesNames = texturesNames;
        this.defaultTextureName = defaultTextureName;


    }
}
