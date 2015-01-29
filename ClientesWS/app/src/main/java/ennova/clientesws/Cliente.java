package ennova.clientesws;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by ASUS K550 on 21/01/2015.
 */

public class Cliente implements KvmSerializable {
    public int IdCliente;
    public String Nombre;
    public int Telefono;

    public Cliente()
    {
        IdCliente = 0;
        Nombre = "";
        Telefono = 0;
    }

    public Cliente(int id, String nombre, int telefono)
    {
        this.IdCliente = id;
        this.Nombre = nombre;
        this.Telefono = telefono;
    }

    @Override
    public Object getProperty(int arg0) {

        switch(arg0)
        {
            case 0:
                return IdCliente;
            case 1:
                return Nombre;
            case 2:
                return Telefono;
        }

        return null;
    }

    @Override
    public int getPropertyCount() {
        return 3;
    }

    @Override
    public void getPropertyInfo(int ind, Hashtable ht, PropertyInfo info) {
        switch(ind)
        {
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdCliente";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Nombre";
                break;
            case 2:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Telefono";
                break;
            default:break;
        }
    }

    @Override
    public void setProperty(int ind, Object val) {
        switch(ind)
        {
            case 0:
                IdCliente = Integer.parseInt(val.toString());
                break;
            case 1:
                Nombre = val.toString();
                break;
            case 2:
                Telefono = Integer.parseInt(val.toString());
                break;
            default:
                break;
        }
    }
}