using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Ennovva.GCM.Core;
using Ennovva.GCM.DAO;


namespace Ennovva.GCM.WebApi.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        [HttpGet]
        public ActionResult ObtenerAplicaciones()
        {
            AplicacionesCore core = new AplicacionesCore();
            try
            {
                return Json(core.ObtenerAplicaciones(), JsonRequestBehavior.AllowGet);
            }
            catch
            {
                return Json(null, JsonRequestBehavior.AllowGet);
            }
        }

        [HttpPost]
        public ActionResult EnviarMensajes(int idAplicacion, string titulo, string mensaje)
        {
            DispositivosCore core = new DispositivosCore();
            bool result = true;
            Helpers.EnviarNotificacionesAndroid enviar = new Helpers.EnviarNotificacionesAndroid();
            List<Dispositivos> dispositivos = new List<Dispositivos>();
            dispositivos = core.ObtenerDispositivosPorAPlicacion(idAplicacion);
            if (dispositivos.Count > 0)
            {
                foreach (var dispo in dispositivos)
                {
                    try
                    {
                        enviar.enviarMensajePrueba(dispo.Serial, mensaje, titulo);

                    }
                    catch
                    {
                        result = false;
                    }
                }
                return Json(result, JsonRequestBehavior.AllowGet);
            }
            else
                return Json("No se encontraron dispositivos registrados a la aplicación", JsonRequestBehavior.AllowGet);

        }

        public ActionResult About()
        {
            ViewBag.Message = "Your application description page.";

            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }

        public ActionResult Chat()
        {

            return View();
        }
    }
}