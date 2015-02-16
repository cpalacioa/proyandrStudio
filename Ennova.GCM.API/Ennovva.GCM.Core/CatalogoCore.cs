using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Ennovva.GCM.DAO;

namespace Ennovva.GCM.Core
{
    public class CatalogoCore
    {
       
        public List<LikesUsuario>ObtTendenciaPorApp(int app)
        {
            return TrendingDAO.ObtTendenciaPorApp(app);
        }

        public bool GuardarLike(LikesUsuario like)
        {
            try
            {
                TrendingDAO.GuardarLike(like);
                return true;
            }
            catch(Exception ex)
            {
                //guardar auditoria
                return false;
            }
        }



    }
}
