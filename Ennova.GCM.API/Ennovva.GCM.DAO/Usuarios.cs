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
    
    public partial class Usuarios
    {
        public Usuarios()
        {
            this.LikesUsuario = new HashSet<LikesUsuario>();
        }
    
        public int Id { get; set; }
        public int IdAplicacion { get; set; }
        public string NombreUsuario { get; set; }
        public string IdUsuario { get; set; }
        public int IdProveedorIdentidad { get; set; }
    
        public virtual Aplicacion Aplicacion { get; set; }
        public virtual ICollection<LikesUsuario> LikesUsuario { get; set; }
        public virtual ProveedorIdentidad ProveedorIdentidad { get; set; }
    }
}
