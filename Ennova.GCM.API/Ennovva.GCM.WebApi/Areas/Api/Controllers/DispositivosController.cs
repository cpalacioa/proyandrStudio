using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Ennovva.GCM.DAO;
using Ennovva.GCM.Core;


namespace Ennovva.GCM.WebApi.Areas.Api.Controllers
{
    public class DispositivosController : Controller
    {
        // GET: Api/Dispositivos
        public ActionResult Index()
        {
            return View();
        }
        [HttpGet]
        public JsonResult Dispositivos()
        {
            DispositivosCore core = new DispositivosCore();
            return Json(core.ObtenerDispositivos(), JsonRequestBehavior.AllowGet);
        }

        public JsonResult Dispositivo(int? id, Dispositivos dispositivo)
        {
            DispositivosCore core = new DispositivosCore();
            switch (Request.HttpMethod)
            {
                case "POST":
                    return Json(core.GuardarDispositivo(dispositivo));
                case "GET":
                    return Json(core.ObtenerDispositivosPorAPlicacion(id.GetValueOrDefault()),
                                JsonRequestBehavior.AllowGet);
            }
            return Json(new { Error = true, Message = "Operación HTTP desconocida" });

        }
    }
}
