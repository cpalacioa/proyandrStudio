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
    
    public partial class LikesUsuario
    {
        public int Id { get; set; }
        public int IdAplicacion { get; set; }
        public int IdProducto { get; set; }
        public Nullable<bool> usrLike { get; set; }
        public int idUsuario { get; set; }
    
        public virtual Aplicacion Aplicacion { get; set; }
        public virtual Usuarios Usuarios { get; set; }
    }
}
