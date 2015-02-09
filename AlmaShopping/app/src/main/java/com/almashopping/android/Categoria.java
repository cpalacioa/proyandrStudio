package com.almashopping.android;


public class Categoria {

    public int Id;
    public String Nombre;
    public String Descripcion;
    public String Foto;

    public Categoria(int p_id,String p_Nombre, String p_Descripcion,String p_Foto) {

        Id=p_id;
        Nombre=p_Nombre;
        Descripcion = p_Descripcion;
        Foto=p_Foto;
    }
}
