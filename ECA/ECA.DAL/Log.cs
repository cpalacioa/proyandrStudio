using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ECA.DAL
{
    public class Log
    {
        public Boolean Answer { get; set; }
        public String  Message { get; set; }

        public Log(Boolean answer,String message)
        {
            this.Answer = answer;
            this.Message = message;
        }
    }


}
