using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ECA.DAL
{
    public class Usuario
    {
        public int    Id          { get; set; }
        public String Username    { get; set; }
        public String Nombre      { get; set; }
        public String Apellido    { get; set; }
        public String Email       { get; set; }
        public String Descripcion { get; set; }

    }
}
