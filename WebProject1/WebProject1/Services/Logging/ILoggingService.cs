namespace WebProject1.Services
{
    using System;

    public interface ILoggingService
    {
        void Log(Exception exception);
    }
}
