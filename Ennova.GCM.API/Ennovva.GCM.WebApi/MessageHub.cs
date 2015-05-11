using Microsoft.AspNet.SignalR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ennovva.GCM.WebApi
{
    public class MessageHub : Hub
    {
        public static event Action<string, string> MessageReceived = delegate { };

        public void SendMessage(string name, string message)
        {
            MessageReceived(name, message);
        }

    }

}
