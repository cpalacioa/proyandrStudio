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
    public class CatalogoController : Controller
    {
        // GET: Api/Catalogo
        public ActionResult Index()
        {
            return View();
        }

        public JsonResult ObtTendenciaApp(int app)
        {
            List<LikesUsuario> likes = new List<LikesUsuario>();
            List<Tendencia> tendencias = new List<Tendencia>();
            CatalogoCore catalogoCore = new CatalogoCore();
            likes = catalogoCore.ObtTendenciaPorApp(app);
            var trends=likes.GroupBy(s=>s.IdProducto);

            if (trends.Count()>0)
            {
                foreach(var item in trends)
                {
                    tendencias.Add(new Tendencia { IdProducto = item.Key, CantidadLikes = item.Count() });


                }
            }
            return Json(tendencias.OrderBy(s => s.CantidadLikes), JsonRequestBehavior.AllowGet);
        }


        public JsonResult Tendencia(LikesUsuario like)
        {
            CatalogoCore core = new CatalogoCore();
            switch (Request.HttpMethod)
            {
                case "POST":
                    return Json(core.GuardarLike(like));
            }
            return Json(new { Error = true, Message = "Operación HTTP desconocida" });

        }

    }
}