using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ECA.DAL
{
    public class NewsDAL
    {

        public List<EC_News> GetAllNews()
        {
            ECAEntities contexto = new ECAEntities();
            return contexto.EC_News.ToList();
        }
    }
}
