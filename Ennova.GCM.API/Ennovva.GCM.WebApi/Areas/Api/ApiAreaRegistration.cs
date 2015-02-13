using System.Web.Mvc;

namespace Ennovva.GCM.WebApi.Areas.Api
{
    public class ApiAreaRegistration : AreaRegistration 
    {
        public override string AreaName 
        {
            get 
            {
                return "Api";
            }
        }

        public override void RegisterArea(AreaRegistrationContext context) 
        {
            context.MapRoute(
                "Api_default",
                "Api/{controller}/{action}/{id}",
                new { action = "Index", id = UrlParameter.Optional }
            );

            //Obtener lista de dispositivos
            context.MapRoute(
                "AccesoDispositivos",
                "Api/Dispostivos",
                new { controller = "Dispositivos", action = "Dispositivos" }
            );

            context.MapRoute(
                "AccesoCliente",
                "Api/Clientes/Cliente/{id}",
            new { controller = "Dispositivos", action = "Dispositivo", id = UrlParameter.Optional }
            );

            //Obtener lista de ubicaciones
            context.MapRoute(
                "lugares",
                "Api/Ubicaciones",
                new { controller = "Ubicaciones", action = "Ubicaciones" }
            );

            context.MapRoute(
               "lugaresubicacion",
               "Api/Ubicaciones/UbicacionesPorUbicacion/{id}",
            new { controller = "Ubicaciones", action = "UbicacionesPorUbicacion", id = UrlParameter.Optional });


        }
    }
}