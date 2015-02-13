package com.parchegram;

public class Ubicacion {

    public int Id;
    public String Nombre;
    public Double latitud;
    public Double longitud;
    public String descripcion;
    public String categoria;
    public String rutaCategoria;

    public  Ubicacion(int _id,String _pnombre,Double _platitud,Double _plongitud,String _pdescripcion,String _pcategoria,String pruta)
    {
        Id=_id;
        Nombre=_pnombre;
        latitud=_platitud;
        longitud=_plongitud;
        descripcion=_pdescripcion;
        categoria=_pcategoria;
        rutaCategoria=pruta;
    }

    public int GetId(){
        return  Id;
    }

}
