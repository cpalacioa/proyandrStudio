using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ennovva.GCM.DAO
{
    public class TrendingDAO
    {
        public static List<LikesUsuario> ObtTendenciaPorApp(int IdAplicacion)
        {
            List<LikesUsuario>listaTrending=new List<LikesUsuario>();
            using (GCMEntidades conexion=new GCMEntidades())
            {

                conexion.Configuration.ProxyCreationEnabled = false;
                listaTrending = conexion.LikesUsuario.Where(l => l.usrLike == true && l.IdAplicacion==IdAplicacion).ToList<LikesUsuario>();

            }
            return listaTrending;
        }

        public static void GuardarLike(LikesUsuario like)
        {
            GCMEntidades contexto = new GCMEntidades();
            contexto.LikesUsuario.Add(like);
            contexto.SaveChanges();
            contexto.Dispose();

        }



    }
}
