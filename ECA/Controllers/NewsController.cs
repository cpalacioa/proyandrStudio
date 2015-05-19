using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using ECA.Models;
using ECA.DAL;
using System.Data.Entity.Validation;
using System.Text;

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
                ec_news.Author = 1;
                db.EC_News.Add(ec_news);

                try
                {
                    db.SaveChanges();
                    return RedirectToAction("Index");
                }
                catch (DbEntityValidationException ex)
                {
                    StringBuilder sb = new StringBuilder();

                    foreach (var failure in ex.EntityValidationErrors)
                    {
                        sb.AppendFormat("{0} failed validation\n", failure.Entry.Entity.GetType());
                        foreach (var error in failure.ValidationErrors)
                        {
                            sb.AppendFormat("- {0} : {1}", error.PropertyName, error.ErrorMessage);
                            sb.AppendLine();
                        }
                    }

                    throw new DbEntityValidationException(
                        "Entity Validation Failed - errors follow:\n" +
                        sb.ToString(), ex
                    ); 
                }
               
               
            }

            return View(_new);
        }


    }
}


