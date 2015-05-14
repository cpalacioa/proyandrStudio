using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using ECA.Models;
using ECA.DAL;

namespace ECA.Controllers
{
    public class NewsController : Controller
    {
        private ECAEntities db = new ECAEntities();

        public ActionResult Index()
        {
            var news = NewsViewModel.GetAllNews(5);
            return View(news);
        }

        public ActionResult Create()
        {

            return View();
        }


        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create(NewsViewModel _new)
        {
            //To do tomar el usuario en sesión
            _new.Author = 1;
            if (ModelState.IsValid)
            {
                EC_News ec_news = new EC_News();
                ec_news=NewsViewModel.TranslateNewsViewModel(_new);
                db.EC_News.Add(ec_news);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(_new);
        }


    }
}


