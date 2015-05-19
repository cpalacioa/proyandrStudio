using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using ECA.DAL;
using ECA.Models;

namespace ECA.Controllers
{
    public class AdminController : Controller
    {

        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Users()
        {
            ECAEntities db=new ECAEntities();
            var usuarios = db.EC_Users.ToList();
            return View(usuarios);
        }

        public ActionResult Articles()
        {
            var articles = ContentModel.getAllContentByType(2);
            return View(articles);

        }

        public ActionResult Categories()
        {
            return View();
        }

        public ActionResult Content()
        {
            return View();
        }


    }
}
