package ennova.democtrlseleccion;

import java.util.Date;

public class Contacto {
 private String Nombre;
 private String Texto;
 private Date Fecha;

    public Contacto(String nombre,String texto, Date fecha)
    {
        Nombre=nombre;
        Texto=texto;
        Fecha=fecha;
    }

    public String GetNombre(){
        return  Nombre;
    }

    public String GetTexto(){
        return Texto;
    }

    public Date getFecha(){
        return Fecha;
    }
}

