using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Ennovva.GCM.DAO;

namespace Ennovva.GCM.Core
{
    public class AplicacionesCore
    {
        public List<Aplicacion>ObtenerAplicaciones()
        {
            return AplicacionesDAO.ObtenerAplicaciones();
        }
    }
}
