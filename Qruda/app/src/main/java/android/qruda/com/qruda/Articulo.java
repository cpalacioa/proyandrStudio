package android.qruda.com.qruda;

public class Articulo {


    public int id;
    public String Nombre;
    public String Imagen;
    public String Fecha;
    public String Link;

    public Articulo(int p_id, String p_nombre, String p_imagen, String p_fecha, String p_link) {
        id = p_id;
        Nombre = p_nombre;
        Imagen = p_imagen;
        Fecha = p_fecha;
        Link = p_link;
    }
}