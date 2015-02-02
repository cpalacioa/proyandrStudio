using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Ennova.GCM.AccesoDatos
{
    public class DispositivosDAO
    {

        public static void GuardarDispositivo(Dispositivos dispositivo)
        {
            GCMEntidades contexto = new GCMEntidades();
            contexto.Dispositivos.Add(dispositivo);
            contexto.SaveChanges();
        }

        public static Dispositivos ObtenerDispositivoPorId(int id)
        {
            GCMEntidades contexto = new GCMEntidades();
            Dispositivos dispositivo = new Dispositivos();
            dispositivo = contexto.Dispositivos.Where(d => d.Id == id).FirstOrDefault();
            return dispositivo;
        }

        public static Dispositivos ObtenerDispositivoPorSerial(int aplicacion, string serial)
        {
            GCMEntidades contexto = new GCMEntidades();
            Dispositivos dispositivo = new Dispositivos();
            dispositivo = contexto.Dispositivos.Where(d => d.Serial == serial && d.IdAplication == aplicacion).FirstOrDefault();
            return dispositivo;
        }

        public static Dispositivos ObtenerDispositivoPorUsuario(int aplicacion, string usuario)
        {
            GCMEntidades contexto = new GCMEntidades();
            Dispositivos dispositivo = new Dispositivos();
            dispositivo = contexto.Dispositivos.Where(d => d.IdUsuario == usuario && d.IdAplication == aplicacion).FirstOrDefault();
            return dispositivo;

        }

        public static bool ActualizarDispositivo(Dispositivos dispositivo)
        {
            GCMEntidades contexto = new GCMEntidades();
            Dispositivos dispositivotmp = new Dispositivos();
            dispositivotmp = ObtenerDispositivoPorUsuario(dispositivo.IdAplication, dispositivo.IdUsuario);
            if (dispositivotmp != null || dispositivotmp.Id > 0)
            {
                dispositivotmp.Serial = dispositivo.Serial;
                dispositivotmp.IdUsuario = dispositivo.IdUsuario;
                contexto.SaveChanges();
                return true;
            }
            else
                return false;
        }

        public static List<Dispositivos> ObtenerDispositivos()
        {
            List<Dispositivos> dispositivos = new List<Dispositivos>();
            GCMEntidades conexion = new GCMEntidades();
            dispositivos = conexion.Dispositivos.ToList();
            return dispositivos;
        }

        public static List<Dispositivos> ObtenerDispositivosApp(int IdAplicacion)
        {
            List<Dispositivos> dispositivos = new List<Dispositivos>();
            GCMEntidades conexion = new GCMEntidades();
            dispositivos = conexion.Dispositivos.Where(d => d.IdAplication == IdAplicacion).ToList();
            return dispositivos;
        }
    }
}
