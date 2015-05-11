using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using clientewsValemas.valemas;

namespace clientewsValemas
{
    class Program
    {
        static void Main(string[] args)
        {
            Usuarios usuarios = new Usuarios();
            
           clientewsValemas.valemas.InicioSesion inicio=usuarios.IniciarSesionAfiliado("afiliadoprueba", "prafiliadoprueba");
           Console.WriteLine(inicio.SessionID);
           Console.ReadLine();
        }
    }
}
