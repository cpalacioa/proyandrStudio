using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using ECA.DAL;

namespace ECA.Controllers
{
    public class News2Controller : Controller
    {
        private ECAEntities db = new ECAEntities();


        public ActionResult Index()
        {
            var ec_news = db.EC_News.Include(e => e.EC_Users);
            return View(ec_news.ToList());
        }

        public ActionResult Details(int id = 0)
        {
            EC_News ec_news = db.EC_News.Find(id);
            if (ec_news == null)
            {
                return HttpNotFound();
            }
            return View(ec_news);
        }


        public ActionResult Create()
        {
            ViewBag.Author = new SelectList(db.EC_Users, "Id", "Username");
            return View();
        }

       
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create(EC_News ec_news)
        {
            if (ModelState.IsValid)
            {
                db.EC_News.Add(ec_news);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.Author = new SelectList(db.EC_Users, "Id", "Username", ec_news.Author);
            return View(ec_news);
        }

       
        public ActionResult Edit(int id = 0)
        {
            EC_News ec_news = db.EC_News.Find(id);
            if (ec_news == null)
            {
                return HttpNotFound();
            }
            ViewBag.Author = new SelectList(db.EC_Users, "Id", "Username", ec_news.Author);
            return View(ec_news);
        }

        //
        // POST: /News2/Edit/5

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit(EC_News ec_news)
        {
            if (ModelState.IsValid)
            {
                db.Entry(ec_news).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.Author = new SelectList(db.EC_Users, "Id", "Username", ec_news.Author);
            return View(ec_news);
        }

        //
        // GET: /News2/Delete/5

        public ActionResult Delete(int id = 0)
        {
            EC_News ec_news = db.EC_News.Find(id);
            if (ec_news == null)
            {
                return HttpNotFound();
            }
            return View(ec_news);
        }

        //
        // POST: /News2/Delete/5

        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            EC_News ec_news = db.EC_News.Find(id);
            db.EC_News.Remove(ec_news);
            db.SaveChanges();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            db.Dispose();
            base.Dispose(disposing);
        }
    }
}