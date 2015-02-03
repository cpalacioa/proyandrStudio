using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ennovva.GCM.DAO
{
    public class AplicacionesDAO
    {
        public static  List<Aplicacion> ObtenerAplicaciones()
        {
            GCMEntidades contexto = new GCMEntidades();
            List<Aplicacion> aplicaciones = new List<Aplicacion>();
            contexto.Configuration.ProxyCreationEnabled = false;
            aplicaciones = contexto.Aplicacion.ToList();
            contexto.Dispose();
            return aplicaciones;
        }
    }
}
