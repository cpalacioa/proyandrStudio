using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Ennovva.GCM.DAO;

namespace Ennovva.GCM.Core
{
    public class DispositivosCore
    {
        public string GuardarDispositivo(Dispositivos dispositivo)
        {

            try
            {
                //Busco el dispositivo ya sea por usuario o por serial para actualizar o guardar

                if (DispositivosDAO.ObtenerDispositivoPorSerial(dispositivo.IdAplication, dispositivo.Serial) != null)
                {
                    DispositivosDAO.ActualizarDispositivo(dispositivo);
                }
               /* if (DispositivosDAO.ObtenerDispositivoPorUsuario(dispositivo.IdAplication, dispositivo.IdUsuario) != null)
                {
                    DispositivosDAO.ActualizarDispositivo(dispositivo);
                }*/
                else
                {
                    DispositivosDAO.GuardarDispositivo(dispositivo);
                }

                return true.ToString();
            }
            catch (Exception ex)
            {
                //Registrar auditoria
                return false.ToString();
            }
        }

        public List<Dispositivos> ObtenerDispositivos()
        {
            return DispositivosDAO.ObtenerDispositivos();
        }

        public List<Dispositivos> ObtenerDispositivosPorAPlicacion(int Id)
        {
            return DispositivosDAO.ObtenerDispositivosApp(Id);
        }


    }
}
