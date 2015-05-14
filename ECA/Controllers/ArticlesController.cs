using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using ECA.Models;


namespace ECA.Controllers
{
    public class ArticlesController : Controller
    {

        public ActionResult Index()
        {
            var articles = ContentModel.GetContentbyType(2, 5);
            return View(articles);
        }

        public ActionResult Serie(int? Id)
        {
          
            return View();
        }

        public ActionResult Article(int Id)
        {
            var detalle = ContentModel.GetContentbyIdParent(Id);

            return View(detalle);
        }


    }
}
