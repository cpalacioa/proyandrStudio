package android.qruda.com.qruda;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ProductListAdapterWithCache extends ArrayAdapter<Producto>{

    private Context contexto;
    List<Producto> productos;
    public int ciclo;

    public ProductListAdapterWithCache(Fragment _contexto, List<Producto> _productos) {

        super(_contexto.getActivity(), R.layout.catalogo_grid_item, _productos);
        contexto=_contexto.getActivity();
        this.productos = _productos;
        ciclo=0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Producto producto = getItem(position);
        ProductViewHolder holder;
        if (convertView == null) {
            convertView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.catalogo_grid_item, parent, false);

            //
            holder = new ProductViewHolder();
            holder.img = (ImageView)convertView.findViewById(R.id.image);
            holder.title = (TextView)convertView.findViewById(R.id.titulo);
            holder.price=(TextView)convertView.findViewById(R.id.valor);
            holder.autor=(TextView)convertView.findViewById(R.id.autor);
            //
            convertView.setTag(holder);
        }
        else{
            holder = (ProductViewHolder) convertView.getTag();
        }


        //
        holder.populate(producto, false);

        //
        return convertView;
    }

    public class ProductViewHolder {
        public ImageView img;
        public TextView title;
        public TextView price;
        public TextView autor;

        void populate(Producto p) {
            title.setText(p.titulo.toUpperCase());
            price.setText("$"+ p.valor.toString().toUpperCase()+" COP");
            autor.setText("BY "+ p.disenador.toUpperCase());
            //ImageDownloader imageDownloader = new ImageDownloader();
            //imageDownloader.download(p.img_url, img);
            Picasso.with(contexto)
                    .load("https://cms-assets.tutsplus.com/uploads/users/21/posts/19431/featured_image/CodeFeature.jpg")
                    .placeholder(R.drawable.spinner)
                    .into(img);
        }

        void populate(Producto p, boolean isBusy) {
            title.setText(p.titulo.toUpperCase());
            price.setText("$"+ p.valor.toString().toUpperCase()+" COP");
            autor.setText("BY " + p.disenador.toUpperCase());

            if (!isBusy){
                // download from internet
/*                Downloader loading=new Downloader();
                loading.execute(Integer.toString(ciclo));


                ImageDownloader imageDownloader = new ImageDownloader();
                imageDownloader.download(p.img_url, img);*/
                Picasso.with(contexto)
                        .load(p.img_url)
                        .placeholder(R.drawable.spinner)
                        .into(img);


            }
            else{
                // set default image

                img.setImageResource(R.drawable.spinner);

            }
        }
    }



    public class Downloader extends AsyncTask<String,Integer,Bitmap> {
        private ProgressDialog progressdialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ciclo=ciclo+1;
            progressdialog= new ProgressDialog(contexto);
            progressdialog.setMessage("Cargando Imagenes");
            progressdialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... arg0) {
            //Log.d(arg0[0].toString(),"");
          //  return download_Image(arg0[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap downloadedImage) {
            super.onPostExecute(downloadedImage);
            ciclo=0;
            //img.setImageBitmap(downloadedImage);
            progressdialog.dismiss();
        }

        private Bitmap download_Image(String url) {
            //---------------------------------------------------
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e("Hub","Error getting the image from server : " + e.getMessage().toString());
            }
            return bm;
            //---------------------------------------------------
        }

    }

}
