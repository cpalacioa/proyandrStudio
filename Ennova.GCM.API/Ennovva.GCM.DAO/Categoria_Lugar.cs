//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace Ennovva.GCM.DAO
{
    using System;
    using System.Collections.Generic;
    
    public partial class Categoria_Lugar
    {
        public Categoria_Lugar()
        {
            this.Lugar = new HashSet<Lugar>();
        }
    
        public int Id { get; set; }
        public string Descripcion { get; set; }
        public string Icono { get; set; }
    
        public virtual ICollection<Lugar> Lugar { get; set; }
    }
}
