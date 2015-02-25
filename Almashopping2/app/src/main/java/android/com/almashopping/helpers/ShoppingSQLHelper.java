package android.com.almashopping.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoppingSQLHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de carrito de compras
    String sqlCreate = "CREATE TABLE cartshop (Id INTEGER PRIMARY KEY, IdProducto INTEGER,Titulo TEXT,descripcion TEXT,marca TEXT,valor REAL,imagen TEXT,cantidad INTEGER)";

    public ShoppingSQLHelper(Context contexto, String nombre,
                                SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS cartshop");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }

}
