using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Ennovva.GCM.DAO;

namespace Ennovva.GCM.Core
{
    public class GeolocalizacionCore
    {
        public List<Lugar> ObtenerLugaresPorUbicacion(double latitud,double longitud,double distancia)
        {
            return LugaresDAO.ObtenerLugaresPorDistancia(latitud, longitud, distancia);
        }

        public List<Lugar>ObtenerLugares()
        {
            return LugaresDAO.ObtenerLugares();
        }
    }
}
