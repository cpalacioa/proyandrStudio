package android.com.almashopping.model;

/**
 * Created by ASUS K550 on 27/02/2015.
 */
public class Usuario {
    public int Id;
    public String username;
    public String email;

    public Usuario(int pid,String pusername,String pemail)
    {
        Id=pid;
        username=pusername;
        email=pemail;
    }
}
