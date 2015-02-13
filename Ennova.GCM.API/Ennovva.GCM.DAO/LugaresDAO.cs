using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ennovva.GCM.DAO
{
    public class LugaresDAO
    {

        public static double ToRadian(double val) { return val * (Math.PI / 180); }

        public static double CalcularDistanciaEntredosPuntos(double latitud1, double longitud1, double latitud2, double longitud2)
        {
            double earthRadius = 6371;
            double dLat = ToRadian(latitud2 - latitud1);
            double dLng = ToRadian(longitud2 - longitud1);
            double sindLat = Math.Sin(dLat / 2);
            double sindLng = Math.Sin(dLng / 2);
            double a = Math.Pow(sindLat, 2) + Math.Pow(sindLng, 2)
                    * Math.Cos(ToRadian(latitud1)) * Math.Cos(ToRadian(latitud2));
            double c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));
            double dist = earthRadius * c;
            return dist; 

        }


        public static Categoria_Lugar ObtenerCategoria(int id)
        {

            using (GCMEntidades contexto = new GCMEntidades())
            {
                contexto.Configuration.ProxyCreationEnabled = false;
                Categoria_Lugar categoria = new Categoria_Lugar();
                categoria = contexto.Categoria_Lugar.Where(c => c.Id == id).FirstOrDefault();
                return categoria;
            }
        }

        public static List<Lugar> ObtenerLugares()
         {
             using (GCMEntidades contexto = new GCMEntidades())
             {
                 contexto.Configuration.ProxyCreationEnabled = false;
                 List<Lugar> lugares = new List<Lugar>();
                 lugares = contexto.Lugar.ToList();
                 return lugares;
             }
         }
        

        public static List<Lugar>ObtenerLugaresPorDistancia(double latitud,double longitud,double distancia)
         {
             List<Lugar> lugares = ObtenerLugares();
             List<Lugar> lugaresfiltrados = new List<Lugar>();
             try
             {
                 foreach(var lugar in lugares)
                 {
                     if(CalcularDistanciaEntredosPuntos(latitud,longitud,lugar.latitud,lugar.longitud)<=distancia)
                     {
                         lugaresfiltrados.Add(lugar);
                     }
                 }
             }

             catch(Exception ex)
             {
                 throw;
             }
             return lugaresfiltrados;

         }
    }
}
