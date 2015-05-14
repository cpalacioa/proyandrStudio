using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ECA.DAL;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel;

namespace ECA.Models
{
    public class NewsViewModel
    {
        [Required]
        [DisplayName("Titulo")]
        public string Title { get; set; }

        [Required]
        [DisplayName("Descripcion")]
        public string Description { get; set; }

        [DisplayName("Ruta de la imagen")]
        public string thumbnail { get; set; }

        [Required]
        [DisplayName("Enlace noticia")]
        public string link { get; set; }

        [DisplayName("Autor")]
        public int Author { get; set; }
    

        public static List<EC_News>GetAllNews(int take)
        {
            NewsDAL newsDal = new NewsDAL();
            return newsDal.GetAllNews().Take(take).ToList();

        }

        public static EC_News TranslateNewsViewModel(NewsViewModel model)
        {
            EC_News _new = new EC_News();
            if (model != null)
            {
                _new.Title = model.Title;
                _new.thumbnail = model.thumbnail;
                _new.link = model.thumbnail;
                _new.Description = model.Description;
                _new.Author = model.Author;
                return _new;
            }
            else
                return null;
        }

    }
}
