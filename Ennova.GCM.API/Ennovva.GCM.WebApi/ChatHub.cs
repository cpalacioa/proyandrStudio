using Microsoft.AspNet.SignalR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ennovva.GCM.WebApi
{
    public class ChatHub : Hub
    {
        public void SendMessage(string userName, string message)
        {
            Clients.All.addMessage(userName, message);
        }
    }
}
