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
    
    public partial class Aplicacion
    {
        public Aplicacion()
        {
            this.Dispositivos = new HashSet<Dispositivos>();
            this.LikesUsuario = new HashSet<LikesUsuario>();
            this.Usuarios = new HashSet<Usuarios>();
        }
    
        public int Id { get; set; }
        public string Descripcion { get; set; }
        public bool activa { get; set; }
    
        public virtual ICollection<Dispositivos> Dispositivos { get; set; }
        public virtual ICollection<LikesUsuario> LikesUsuario { get; set; }
        public virtual ICollection<Usuarios> Usuarios { get; set; }
    }
}
