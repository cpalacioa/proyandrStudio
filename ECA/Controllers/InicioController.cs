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

        public ActionResult Index()
        {
            if (!Request.IsAuthenticated)
            {
                return RedirectToAction("login", "Users");
            }
            else
            {
                return View();
            }
        }


        [HttpGet]
        public ActionResult GetLastPost()
        {
            var post = ContentModel.GetLastContent(5);
            return PartialView(@"~/Views/Shared/_LastPost.cshtml", post);

        }

    }
}
