package android.com.almashopping.model;

import com.google.android.gms.analytics.ecommerce.Product;

public class basket {
    public int cantidad;
    public Producto producto;

    public basket (int p_cantidad,Producto _producto) {
        cantidad=p_cantidad;
        producto=_producto;

    }



}
