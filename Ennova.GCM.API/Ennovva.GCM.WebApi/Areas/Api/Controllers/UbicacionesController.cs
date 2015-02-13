using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Ennovva.GCM.DAO;
using Ennovva.GCM.Core;
using Ennovva.GCM.WebApi.Models;

namespace Ennovva.GCM.WebApi.Areas.Api.Controllers
{
    public class UbicacionesController : Controller
    {
        // GET: Api/Ubicaciones
        public ActionResult Index()
        {

            return View();
        }

        public JsonResult UbicacionesPorUbicacion(double latitud,double longitud,double distancia)
        {
            GeolocalizacionCore core = new GeolocalizacionCore();
            List<UbicacionesMaps> maps = new List<UbicacionesMaps>();
            List<Lugar> lugares = core.ObtenerLugaresPorUbicacion(latitud, longitud, distancia);

            if(lugares.Count>0)
                foreach(var lug in lugares)
                {
                    Categoria_Lugar categoria = LugaresDAO.ObtenerCategoria(lug.IdCategoria);
                    maps.Add(new UbicacionesMaps { categoria = categoria, lugar = lug });
                }

            return Json(maps,JsonRequestBehavior.AllowGet);
        }

        public JsonResult Ubicaciones()
        {
            GeolocalizacionCore core = new GeolocalizacionCore();
            List<UbicacionesMaps> maps = new List<UbicacionesMaps>();
            List<Lugar> lugares = core.ObtenerLugares();

            foreach(var lug in lugares)
            {
                Categoria_Lugar categoria = LugaresDAO.ObtenerCategoria(lug.IdCategoria);
                maps.Add(new UbicacionesMaps { categoria = categoria, lugar = lug });
            }

            return Json(maps, JsonRequestBehavior.AllowGet);
        }



    }
}