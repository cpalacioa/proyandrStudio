package android.com.almashopping.helpers.Ennovva;

import android.app.Activity;
import android.com.almashopping.R;
import android.com.almashopping.model.Usuario;
import android.os.StrictMode;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Core extends Activity{


    public Usuario ObtUsuarioRegistrado(String email,int aplicacion,int provider,Activity activity)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        boolean resul = false;
        String urlBase=activity.getResources().getString(R.string.urlRestCore);
        try
        {
            String urlresource =urlBase+"user/getbyparams?email="+email+"&aplication="+aplicacion+"&provider="+provider+"";
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(urlresource);
            HttpResponse response = client.execute(get);
            String respStr = EntityUtils.toString(response.getEntity());
            Log.d("respBuscar",respStr);
            JSONObject obj = new JSONObject(respStr);
            if(obj!=null)
            {
                if(obj.getBoolean("error")==false) {
                    Usuario usuario = new Usuario(obj.getInt("id"),obj.getString("username"),obj.getString("email"));
                    return  usuario;
                }
                else
                    return  null;
            }
            else
                return  null;


        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
            return  null;
        }

    }

   public  Boolean RegistrarDispositivo(Activity activity,String serial,Integer userid)
   {
       StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       StrictMode.setThreadPolicy(policy);
       boolean resul = false;
       String urlBase=activity.getResources().getString(R.string.urlRestCore);
       try
       {
           String urlresource =urlBase+"device/register";
           HttpClient client = new DefaultHttpClient();
           HttpPost post = new HttpPost(urlresource);
           List<NameValuePair> nameValuePairs = new ArrayList<>(4);
           nameValuePairs.add(new BasicNameValuePair("serial", serial));
           nameValuePairs.add(new BasicNameValuePair("aplication",Integer.toString(activity.getResources().getInteger(R.integer.IdAPlicacion))));
           nameValuePairs.add(new BasicNameValuePair("user",Integer.toString(userid)));

           post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
           HttpResponse response = client.execute(post);

           String responseBody = EntityUtils
                   .toString(response.getEntity(), "UTF-8");
           Log.d("respRegistrarDispositivo",responseBody);

       }
       catch(Exception ex)
       {
           Log.e("respRegistrarDispositivo", "Error!", ex);
           resul = false;
       }

       return resul;

   }
    public Boolean RegistrarUsuario(String username,String email,int IdProvider,Activity activity)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        boolean resul = false;
        String urlBase=activity.getResources().getString(R.string.urlRestCore);
        try
        {
            String urlresource =urlBase+"user/register";
            Log.d("respRegistrar",urlresource);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlresource);
            List<NameValuePair> nameValuePairs = new ArrayList<>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("aplication",Integer.toString(activity.getResources().getInteger(R.integer.IdAPlicacion))));
            nameValuePairs.add(new BasicNameValuePair("identityprovider",Integer.toString(IdProvider)));

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);

            String responseBody = EntityUtils
                    .toString(response.getEntity(), "UTF-8");
            Log.d("respRegistrarUsuario",responseBody);

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
            resul = false;
        }

        return resul;
    }

}
