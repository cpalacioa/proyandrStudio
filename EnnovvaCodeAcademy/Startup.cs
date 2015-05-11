using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(EnnovvaCodeAcademy.Startup))]
namespace EnnovvaCodeAcademy
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
