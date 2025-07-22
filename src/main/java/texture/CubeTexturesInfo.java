package texture;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.mesh.MeshType;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CubeTexturesInfo(

    String id,
    MeshType type,
    List<String> textures,
    String defaultTexture
){}
