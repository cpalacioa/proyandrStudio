//------------------------------------------------------------------------------
// <auto-generated>
//    This code was generated from a template.
//
//    Manual changes to this file may cause unexpected behavior in your application.
//    Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace ECA.DAL
{
    using System;
    using System.Collections.Generic;
    
    public partial class EC_TypeContent
    {
        public EC_TypeContent()
        {
            this.EC_Content = new HashSet<EC_Content>();
        }
    
        public int Id { get; set; }
        public string Description { get; set; }
    
        public virtual ICollection<EC_Content> EC_Content { get; set; }
    }
}
