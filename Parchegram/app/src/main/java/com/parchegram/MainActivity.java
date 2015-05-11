package com.parchegram;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends ActionBarActivity {
    private GoogleMap mapa;
    private int vista = 0;
    List<LatLng> listalatitudes;
    List<Ubicacion>ubicaciones;
    Double milatitud;
    Double miLongitud;
    LatLng ubicacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        milatitud=0.0;
        miLongitud=0.0;

        mapa = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        mapa.setMyLocationEnabled(true);
        mapa.getUiSettings().setMyLocationButtonEnabled(true);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.getUiSettings().setZoomGesturesEnabled(true);
        mapa.getUiSettings().setCompassEnabled(true);
        mapa.getUiSettings().setRotateGesturesEnabled(true);
        mapa.setOnMyLocationChangeListener(myLocationChangeListener);

        //Acccion para click en un mapa
        mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                Projection proj = mapa.getProjection();
                Point coord = proj.toScreenLocation(point);

                Toast.makeText(
                        MainActivity.this,
                        "Click\n" +
                                "Lat: " + point.latitude + "\n" +
                                "Lng: " + point.longitude + "\n" +
                                "X: " + coord.x + " - Y: " + coord.y,
                        Toast.LENGTH_SHORT).show();
            }
        });

          //Delegado para click en los marcadores
        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {


            public boolean onMarkerClick(Marker marker) {
                List<LatLng>coordenadas=null;
                 coordenadas=new ArrayList();
                Toast.makeText(
                        MainActivity.this,
                        "Marcador pulsado:\n" +
                                marker.getTitle()+
                        marker.getPosition(),
                        Toast.LENGTH_SHORT).show();
                Location location=mapa.getMyLocation();
                LatLng loc = new LatLng (location.getLatitude(), location.getLongitude());
                coordenadas.add(loc);
                 coordenadas.add(new LatLng(marker.getPosition().latitude,marker.getPosition().longitude));
                mostrarLineas(coordenadas);
                return false;
            }
        });

        //Accion cuando la camara cambia
        /*mapa.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            public void onCameraChange(CameraPosition position) {
                Toast.makeText(
                        MainActivity.this,
                        "Cambio Cámara\n" +
                                "Lat: " + position.target.latitude + "\n" +
                                "Lng: " + position.target.longitude + "\n" +
                                "Zoom: " + position.zoom + "\n" +
                                "Orientación: " + position.bearing + "\n" +
                                "Ángulo: " + position.tilt,
                        Toast.LENGTH_SHORT).show();
            }
        });*/
    }



    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void mostrarLineas(List<LatLng>coordenadas)
    {
        //Dibujo con Lineas
        PolylineOptions lineas = new PolylineOptions();
        for(int coor=0;coor<coordenadas.size();coor++)
        {
            LatLng coordenada=coordenadas.get(coor);
            lineas.add(coordenada);
        }
        lineas.width(8);
        lineas.color(Color.RED);
        Polyline polyline = mapa.addPolyline(lineas);

    }

    private void mostrarMarcador(LatLng location,List<Ubicacion>ubicaciones)
    {

       Log.d("debug",ubicaciones.toString());
        Log.d("debug",location.toString());


      /* listalatitudes=new ArrayList();
       listalatitudes.add(new LatLng(4.62542565,-74.0661001));
       listalatitudes.add(new LatLng(4.702687,-74.028999));*/

        for(int locations=0;locations<ubicaciones.size();locations++) {

            Ubicacion ubicacion=ubicaciones.get(locations);
            LatLng coordenadas=new LatLng(ubicacion.latitud,ubicacion.longitud);
            Log.d("coordenadas",ubicacion.rutaCategoria);
            Double distancia=ObtDistancia(location.latitude,location.longitude,ubicacion.latitud,ubicacion.longitud);
            String txtDistancia="a "+Double.toString(distancia)+" kms";
            BitmapDescriptor image=null;

            try {

                try {
                    Bitmap img = getBitmapFromURL(ubicacion.rutaCategoria);
                    image=BitmapDescriptorFactory.fromBitmap(img);


                } catch (Exception e) {
                    image=BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
                    e.printStackTrace();
                    Log.d("error1",e.toString());
                }
                mapa.addMarker(new MarkerOptions()
                        .icon(image)
                        .position(coordenadas)
                        .title(ubicacion.Nombre));

            } catch (Exception e) {
                image=BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
                e.printStackTrace();
                Log.d("error2",e.toString());

            }
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))

        }
    }


    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<LatLng,Integer,Boolean> {


        protected Boolean doInBackground(LatLng... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            Double distancia=1.0;
            ubicacion=params[0];

           ubicaciones=new ArrayList();

            HttpGet del =new HttpGet("http://mauropalacio.co/Api/ubicaciones/UbicacionesPorUbicacion/?latitud="+ubicacion.latitude+"&longitud="+ubicacion.longitude+"&distancia="+distancia+"");
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);

                for(int i=0; i<respJSON.length(); i++) {

                    JSONObject obj = respJSON.getJSONObject(i);
                    JSONObject lugar=new JSONObject(obj.getString("lugar"));
                    JSONObject categoria=new JSONObject((obj.getString("categoria")));
                    ubicaciones.add(new Ubicacion(lugar.getInt("Id"),lugar.getString("Nombre"),lugar.getDouble("latitud"),lugar.getDouble("longitud"),lugar.getString("Descripcion"),categoria.getString("Descripcion"),categoria.getString("Icono")));
                }

            }
            catch(Exception ex)
            {
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
//                Log.d("debug",ubicaciones.toString());
                mostrarMarcador(ubicacion,ubicaciones);

            }
        }
    }


    GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange (Location location) {
            if(miLongitud==0.0 && milatitud==0.0) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                Marker mMarker = mapa.addMarker(new MarkerOptions().position(loc));
                mMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
               // mostrarMarcador(location.getLatitude(), location.getLongitude(),location);
                milatitud=location.getLatitude();
                miLongitud=location.getLongitude();
                TareaWSListar tarea=new TareaWSListar();
                tarea.execute(loc);

            }
            else
            {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("latitud inicial",Double.toString(milatitud));
                Log.d("latitud actual",Double.toString(location.getLatitude()));
                Double distancia=ObtDistancia(milatitud,miLongitud,location.getLatitude(),location.getLongitude());
                Log.d("latitud distancia",Double.toString(distancia));

                if(distancia>1)
                {
                    mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                    Marker mMarker = mapa.addMarker(new MarkerOptions().position(loc));
                    mMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
                    //mostrarMarcador(location.getLatitude(),location.getLongitude(),location);
                    milatitud=location.getLatitude();
                    miLongitud=location.getLongitude();
                    TareaWSListar tarea=new TareaWSListar();
                    tarea.execute(loc);

                }

            }


        }
    };
    private double ObtDistancia(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        return dist; // output distance, KM
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_main, container, false);
    }

    public void onActivityCreated(Bundle savedState) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_vista:
                alternarVista();
                break;
            case R.id.menu_mover:
                //Centramos el mapa en España
                CameraUpdate camUpd1 =
                        CameraUpdateFactory.newLatLng(new LatLng(40.41, -3.69));
                mapa.moveCamera(camUpd1);
                break;
            case R.id.menu_animar:
                //Centramos el mapa en España y con nivel de zoom 5
                CameraUpdate camUpd2 =
                        CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 5F);
                mapa.animateCamera(camUpd2);
                break;
            case R.id.menu_3d:
                Location location=mapa.getMyLocation();
                LatLng loc = new LatLng (location.getLatitude(), location.getLongitude());
                CameraPosition camPos = new CameraPosition.Builder()
                        .target(loc)   //Centramos el mapa en Madrid
                        .zoom(19)         //Establecemos el zoom en 19
                        .bearing(45)      //Establecemos la orientación con el noreste arriba
                        .tilt(70)         //Bajamos el punto de vista de la cámara 70 grados
                        .build();

                CameraUpdate camUpd3 =
                        CameraUpdateFactory.newCameraPosition(camPos);

                mapa.animateCamera(camUpd3);
                break;
            case R.id.menu_posicion:
                CameraPosition camPos2 = mapa.getCameraPosition();
                LatLng pos = camPos2.target;
                Toast.makeText(MainActivity.this,
                        "Lat: " + pos.latitude + " - Lng: " + pos.longitude,
                        Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }
    private void alternarVista()
    {
        vista = (vista + 1) % 4;

        switch(vista)
        {
            case 0:
                mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case 2:
                mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 3:
                mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
        }
    }

}
