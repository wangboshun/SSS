using FluentValidation.Results;

using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.DI;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SSS.Domain.Seedwork.ErrorHandler
{
    [DIService(ServiceLifetime.Singleton, typeof(IErrorHandler))]
    public class ErrorHandler : IErrorHandler
    {
        private readonly ILogger _logger;
        private readonly List<ValidationFailure> _notice;

        public ErrorHandler()
        {
            _notice = new List<ValidationFailure>();
            _logger = IocEx.Instance.GetService<ILogger<ErrorHandler>>();
        }

        public Task Execute(ValidationResult message)
        {
            _notice.AddRange(message.Errors);
            _logger.LogError("ErrorHandler Execute  " + string.Join(",", message.Errors.Select(t => t.ErrorMessage)));
            return Task.CompletedTask;
        }

        public Task Execute(string message)
        {
            _notice.Add(new ValidationFailure(message, message));
            return Task.CompletedTask;
        }

        public Task Execute(Exception ex)
        {
            _notice.Add(new ValidationFailure(ex.GetType().ToString(), ex.InnerException.Message));
            _logger.LogError(new EventId(ex.HResult), ex, "ErrorHandler Exception");
            return Task.CompletedTask;
        }

        public List<ValidationFailure> GetNotice()
        {
            return _notice;
        }

        public bool HasNotice()
        {
            return GetNotice().Any();
        }

        public void Dispose()
        {
            _notice.Clear();
            GC.SuppressFinalize(this);
        }
    }
}