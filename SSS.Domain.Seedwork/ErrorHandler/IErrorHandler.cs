using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using FluentValidation.Results;

namespace SSS.Domain.Seedwork.ErrorHandler
{
    public interface IErrorHandler : IDisposable
    {
        Task Execute(ValidationResult message);

        Task Execute(string message);

        Task Execute(Exception ex);

        List<ValidationFailure> GetNotice();

        bool HasNotice();
    }
}