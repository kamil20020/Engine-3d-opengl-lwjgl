package texture;

public class CubeTexture implements Cloneable{

    private String name;
    private byte row;
    private byte col;

    public CubeTexture(){

    }

    private CubeTexture(CubeTexture cubeTexture){

        this.name = cubeTexture.name;
        this.row = cubeTexture.row;
        this.col = cubeTexture.col;
    }

    @Override
    protected CubeTexture clone() {

        return new CubeTexture(this);
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public byte getRow() {

        return row;
    }

    public void setRow(byte row) {

        this.row = row;
    }

    public byte getCol() {

        return col;
    }

    public void setCol(byte col) {

        this.col = col;
    }

    @Override
    public String toString(){

        return "CubeTexture(name=" + name + ", row=" + row + ", col=" + col + ")";
    }

}
