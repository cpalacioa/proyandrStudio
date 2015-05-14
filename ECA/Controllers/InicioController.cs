using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using ECA.Models;

namespace ECA.Controllers
{
    public class InicioController : Controller
    {
        //
        // GET: /Inicio/

        public ActionResult Index()
        {
            return View();
        }


        [HttpGet]
        public ActionResult GetLastPost()
        {
            var post = ContentModel.GetLastContent(5);
            return PartialView(@"~/Views/Shared/_LastPost.cshtml", post);

        }
    }
}
