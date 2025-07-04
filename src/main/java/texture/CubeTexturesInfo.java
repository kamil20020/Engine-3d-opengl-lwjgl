package texture;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CubeTexturesInfo(

    String id,
    List<String> textures,
    String defaultTexture
){}
