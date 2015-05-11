using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Web;

namespace Ennovva.GCM.WebApi.Helpers
{
    public class EnviarNotificacionesAndroid
    {
        public bool enviarMensajePrueba(String registration_id, string mensaje, string titulo)
        {
            String GCM_URL = @"https://android.googleapis.com/gcm/send";

            string collapseKey = DateTime.Now.ToString();
            Dictionary<string, string> data = new System.Collections.Generic.Dictionary<string, string>();
            data.Add("data.mensaje",
                HttpUtility.UrlEncode(mensaje));
            data.Add("data.titulo", HttpUtility.UrlEncode(titulo));

            bool flag = false;
            StringBuilder sb = new StringBuilder();

            sb.AppendFormat("registration_id={0}&collapse_key={1}",
                registration_id, collapseKey);

            foreach (string item in data.Keys)
            {
                if (item.Contains("data."))
                    sb.AppendFormat("&{0}={1}", item, data[item]);
            }

            string msg = sb.ToString();
            HttpWebRequest req = (HttpWebRequest)WebRequest.Create(GCM_URL);
            req.Method = "POST";
            req.ContentLength = msg.Length;
            req.ContentType = "application/x-www-form-urlencoded";

            string apiKey = "AIzaSyCnULoUMl5BXgeqcEjO6Gr_9q45dPhTXiU";
            req.Headers.Add("Authorization:key=" + apiKey);

            using (StreamWriter oWriter = new StreamWriter(req.GetRequestStream()))
            {
                oWriter.Write(msg);
            }

            using (HttpWebResponse resp = (HttpWebResponse)req.GetResponse())
            {
                using (StreamReader sr = new StreamReader(resp.GetResponseStream()))
                {
                    string respData = sr.ReadToEnd();

                    if (resp.StatusCode == HttpStatusCode.OK)   // OK = 200
                    {
                        if (respData.StartsWith("id="))
                            flag = true;
                    }
                    else if (resp.StatusCode == HttpStatusCode.InternalServerError)    // 500
                        Console.WriteLine("Error interno del servidor, prueba más tarde.");
                    else if (resp.StatusCode == HttpStatusCode.ServiceUnavailable)    // 503
                        Console.WriteLine("Servidor no disponible temporalmente, prueba más tarde.");
                    else if (resp.StatusCode == HttpStatusCode.Unauthorized)          // 401
                        Console.WriteLine("La API Key utilizada no es válida.");
                    else
                        Console.WriteLine("Error: " + resp.StatusCode);
                }
            }

            return flag;
        }


    }
}