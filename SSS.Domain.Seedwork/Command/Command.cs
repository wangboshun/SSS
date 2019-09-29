using FluentValidation.Results;
using SSS.Domain.Seedwork.Events;
using System;

namespace SSS.Domain.Seedwork.Command
{
    public abstract class Command : Message
    {
        public DateTime Timestamp { get; private set; }
        public ValidationResult ValidationResult { get; set; }

        protected Command()
        {
            Timestamp = DateTime.Now;
        }

        public abstract bool IsValid();
    }
}
