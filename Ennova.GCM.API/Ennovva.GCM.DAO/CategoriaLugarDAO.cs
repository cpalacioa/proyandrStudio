using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ennovva.GCM.DAO
{
    public class CategoriaLugarDAO
    {

        public static void GuardarCategoriaLugar(Categoria_Lugar categoria)
        {
            GCMEntidades contexto = new GCMEntidades();
            contexto.Categoria_Lugar.Add(categoria);
            contexto.SaveChanges();
            contexto.Dispose();
        }

        public static List<Categoria_Lugar> ListarCategorias()
        {
            GCMEntidades contexto = new GCMEntidades();
            List<Categoria_Lugar> categorias = new List<Categoria_Lugar>();
            categorias = contexto.Categoria_Lugar.ToList();
            contexto.Dispose();
            return categorias;
        }




    }
}
