using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ennovva.GCM.DAO
{
    public class DispositivosDAO
    {
        public static void GuardarDispositivo(Dispositivos dispositivo)
        {
            GCMEntidades contexto = new GCMEntidades();
            contexto.Dispositivos.Add(dispositivo);
            contexto.SaveChanges();
            contexto.Dispose();

        }

        public static Dispositivos ObtenerDispositivoPorId(int id)
        {
            GCMEntidades contexto = new GCMEntidades();
            Dispositivos dispositivo = new Dispositivos();
            dispositivo = contexto.Dispositivos.Where(d => d.Id == id).FirstOrDefault();
            contexto.Dispose();
            return dispositivo;
        }

        public static Dispositivos ObtenerDispositivoPorSerial(int aplicacion, string serial)
        {
            GCMEntidades contexto = new GCMEntidades();
            Dispositivos dispositivo = new Dispositivos();
            dispositivo = contexto.Dispositivos.Where(d => d.Serial == serial && d.IdAplication == aplicacion).FirstOrDefault();
            contexto.Dispose();
            return dispositivo;
        }

        public static Dispositivos ObtenerDispositivoPorUsuario(int aplicacion, string usuario)
        {
            GCMEntidades contexto = new GCMEntidades();
            Dispositivos dispositivo = new Dispositivos();
            dispositivo = contexto.Dispositivos.Where(d => d.IdUsuario == usuario && d.IdAplication == aplicacion).FirstOrDefault();
            contexto.Dispose();
            return dispositivo;

        }

        public static bool ActualizarDispositivo(Dispositivos dispositivo)
        {
            GCMEntidades contexto = new GCMEntidades();
            Dispositivos dispositivotmp = new Dispositivos();

            dispositivotmp = ObtenerDispositivoPorUsuario(dispositivo.IdAplication, dispositivo.IdUsuario);


            if (dispositivotmp != null)
            {
                dispositivotmp.Serial = dispositivo.Serial;
                dispositivotmp.IdUsuario = dispositivo.IdUsuario;
                contexto.Dispositivos.Attach(dispositivotmp);
                contexto.SaveChanges();
                contexto.Dispose();
                return true;
            }
            else
            {
                dispositivotmp = ObtenerDispositivoPorSerial(dispositivo.IdAplication, dispositivo.Serial);
                if (dispositivotmp != null)
                {
                    dispositivotmp.Serial = dispositivo.Serial;
                    dispositivotmp.IdUsuario = dispositivo.IdUsuario;
                    contexto.Dispositivos.Attach(dispositivotmp);
                    contexto.SaveChanges();
                    contexto.Dispose();
                    return true;
                }
                else
                    return false;
            }
        }

        public static List<Dispositivos> ObtenerDispositivos()
        {
            List<Dispositivos> dispositivos = new List<Dispositivos>();
            GCMEntidades conexion = new GCMEntidades();
            conexion.Configuration.ProxyCreationEnabled = false;
            dispositivos = conexion.Dispositivos.ToList<Dispositivos>();
            conexion.Dispose();
            return dispositivos;
        }

        public static List<Dispositivos> ObtenerDispositivosApp(int IdAplicacion)
        {
            List<Dispositivos> dispositivos = new List<Dispositivos>();
            GCMEntidades conexion = new GCMEntidades();
            conexion.Configuration.ProxyCreationEnabled = false;
            dispositivos = conexion.Dispositivos.Where(d => d.IdAplication == IdAplicacion).ToList();
            return dispositivos;
        }
    }

}

