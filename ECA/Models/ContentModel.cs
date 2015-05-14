using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ECA.DAL;


namespace ECA.Models
{
    public class ContentModel
    {

        public static List<EC_Content> GetLastContent(int take)
        {
            ContentDAL contentDal = new ContentDAL();

            return contentDal.GetAllContent().Take(take).ToList();
        }

        public static List<EC_Content>GetContentbyType(int type,int take)
        {
            ContentDAL contentDal = new ContentDAL();
            return contentDal.GetAllContentByType(type).Take(take).ToList();
        }


        public static EC_DetailContent GetContentbyIdParent(int Id)
        {
            ContentDAL contentDal = new ContentDAL();

            return contentDal.GetContentbyIdParent(Id);
        }

        public static Serie GetSerieFromContent(int IdSerie)
        {
            ContentDAL contentDal = new ContentDAL();
            Serie serie=new Serie();
            var serietmp=contentDal.GetContentById(IdSerie);
            var childseries = contentDal.GetAllContentByParent(IdSerie);
            if(serietmp!=null)
            {
                serie.autor=serietmp.EC_Users.Username;
                serie.Description=serietmp.Description;
                serie.Id=serietmp.Id;
                serie.image=serietmp.thumbail;
                serie.title=serietmp.Title;
                serie.series = childseries;

            }

            return serie;
        }
    }
}
