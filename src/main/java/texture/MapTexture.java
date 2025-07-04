package texture;

public class MapTexture {

    private String id;
    private String path;
    private byte row;
    private byte col;

    public MapTexture(String id, String path, byte row, byte col) {

        this.id = id;
        this.path = path;
        this.row = row;
        this.col = col;
    }

    public MapTexture() {


    }

    public String getId() {

        return id;
    }

    public String getPath() {

        return path;
    }

    public byte getRow() {

        return row;
    }

    public byte getCol() {

        return col;
    }

}
