using FluentValidation.Results;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace SSS.Domain.Seedwork.ErrorHandler
{
    public interface IErrorHandler : IDisposable
    {
        Task Execute(FluentValidation.Results.ValidationResult message);

        Task Execute(Exception ex);

        List<ValidationFailure> GetNotice();

        bool HasNotice(); 
    }
}
