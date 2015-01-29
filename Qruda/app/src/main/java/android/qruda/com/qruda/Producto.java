package android.qruda.com.qruda;


public class Producto {

    public int id ;
    public String titulo;
    public String img_url;
    public String valor;
    public String disenador;
    public String link;

    public Producto(int p_id,String p_titulo, String p_img_url,String p_valor,String p_disenador) {
        id=p_id;
        titulo=p_titulo;
        img_url = p_img_url;
        valor=p_valor;
        disenador=p_disenador;
    }
    public int GetId(){
        return  id;
    }

    public String GetTitulo(){
        return titulo;
    }

    public String Valor(){
        return valor;
    }
}
