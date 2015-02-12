package com.almashopping.android;


public class Producto {

    public int id ;
    public String titulo;
    public String img_url;
    public String valor;
    public String marca;
    public String link;
    public String descripcion;

    public Producto(int p_id,String p_titulo, String p_img_url,String p_valor,String p_marca,String p_descripcion) {
        id=p_id;
        titulo=p_titulo;
        img_url = p_img_url;
        valor=p_valor;
        marca=p_marca;
        descripcion=p_descripcion;
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
