[assembly: Microsoft.Owin.OwinStartup(typeof(WebProject1.Startup))]

namespace WebProject1
{
    using System.Web.Mvc;
    using Owin;

    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureContainer(app);
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
        }
    }
}
