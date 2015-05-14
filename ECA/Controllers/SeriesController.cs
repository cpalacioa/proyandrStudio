using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using ECA.Models;

namespace ECA.Controllers
{
    public class SeriesController : Controller
    {
        //
        // GET: /Series/

        public ActionResult Index()
        {
            var series = ContentModel.GetContentbyType(1, 5);
            return View(series);
        }

        public ActionResult Serie(int? Id)
        {
            var series = ContentModel.GetSerieFromContent(int.Parse(Id.ToString()));
            return View(series);
        }

        public ActionResult Capitulo(int? Id)
        {
            return View();
        }


    }
}
