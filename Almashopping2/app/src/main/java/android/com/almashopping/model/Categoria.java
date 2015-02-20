package android.com.almashopping.model;


import java.util.ArrayList;
import java.util.List;

public class Categoria {

    public int Id;
    public String Nombre;
    public String Descripcion;
    public String Foto;
    public Integer IdPadre;
    public List<LlaveValor>hijas;

    public Categoria(){

    }
    public Categoria(int p_id,String p_Nombre, String p_Descripcion,String p_Foto,Integer p_IdPadre) {

        Id=p_id;
        Nombre=p_Nombre;
        Descripcion = p_Descripcion;
        Foto=p_Foto;
        IdPadre=p_IdPadre;
    }
    public void setId(int p_id ){
        Id=p_id;
    }

    public int getId()
    {
        return Id;
    }

    public void setNombre(String p_Nombre ){
        Nombre=p_Nombre;
    }

    public String getNombre()
    {
        return Nombre;
    }
    public void setDescripcion(String p_Descripcion ){
        Descripcion=p_Descripcion;
    }

    public String getDescripcion()
    {
        return Descripcion;
    }
    public void setFoto
            (String p_Foto ){
        Foto=p_Foto;
    }

    public String getFoto()
    {
        return Foto;
    }
    public void setIdPadre(int p_idpadre ){
        IdPadre=p_idpadre;
    }

    public int getIdPadre()
    {
        return IdPadre;
    }

    public void setHijas(List<LlaveValor>p_hijas)
    {
        hijas=p_hijas;
    }
    public List<LlaveValor> getHijas()
    {
        return hijas;
    }
    public  List<Categoria>FiltrarPorPadres(List<Categoria>categorias,Integer padre)
    {
        List<Categoria>categoriafiltrada=new ArrayList();
        for(Categoria cate:categorias)
        {
            if(cate.IdPadre==padre)
                categoriafiltrada.add(cate);
        }
        return  categoriafiltrada;
    }

     public List<LlaveValor>obtenerHijas(List<Categoria>categorias,Integer padre)
     {
         List<Categoria>categoriastmp=new ArrayList();
         List<LlaveValor>hijas=new ArrayList<LlaveValor>();
         categoriastmp=FiltrarPorPadres(categorias,padre);
         for (Categoria cate:categoriastmp) {
           hijas.add(new LlaveValor(cate.Id,cate.Nombre));
         }
         return hijas;
     }

}
