using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ECA.DAL;

namespace ECA.Models
{
    public class Serie
    {
        public int Id { get; set; }
        public String title { get; set; }
        public String autor { get;set; }
        public String image { get; set; }
        public String Description { get; set; }
        public List<EC_Content>series{get;set;}

    }
}
