using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ECA.DAL
{
    public class ContentDAL
    {
        public List<EC_TypeContent> GetAllTypeContent()
        {
            ECAEntities contexto=new ECAEntities();
            return contexto.EC_TypeContent.ToList();
        }

        public EC_Content GetContentById(int id)
        {
            ECAEntities contexto = new ECAEntities();
            return contexto.EC_Content.Where(s => s.Id == id).FirstOrDefault();
        }

        public List<EC_Content> GetAllContent()
        {
            ECAEntities contexto = new ECAEntities();
            return contexto.EC_Content.ToList();
        }

        public List<EC_Content> GetAllContentByType(int typeContent)
        {
            ECAEntities contexto = new ECAEntities();
            return contexto.EC_Content.Where(cont => cont.IdTypeContent == typeContent && cont.IdParent==null).ToList();
        }

        public List<EC_Content>GetAllContentByParent(int parent)
        {
            ECAEntities contexto = new ECAEntities();
            return contexto.EC_Content.Where(cont => cont.IdParent == parent).ToList();
        }

        public EC_DetailContent getDetailContentById(int content)
        {
            ECAEntities contexto = new ECAEntities();
            return contexto.EC_DetailContent.Where(detail => detail.Id == content).FirstOrDefault();
        }


        public List<EC_Content> GetAllContentByAuthor(int usuario)
        {
            ECAEntities contexto = new ECAEntities();
            return contexto.EC_Content.Where(content => content.IdUserAuthor == usuario).ToList();
        }

        public EC_DetailContent GetContentbyIdParent(int idParent)
        {
            ECAEntities contexto = new ECAEntities();

            return contexto.EC_DetailContent.Where(detail => detail.IdContent == idParent).FirstOrDefault();
        }


    }
}
