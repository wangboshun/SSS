using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.System.Job.JobError.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.System.Job.JobError.Validation
{
    [DIService(ServiceLifetime.Singleton, typeof(IValidator<JobErrorInputDto>))]
    public class JobErrorAddValidation : AbstractValidator<JobErrorInputDto>
    {
        public JobErrorAddValidation()
        {
            RuleSet("Insert", () =>
            {

            });

            RuleSet("Delete", () =>
            {

            });

            RuleSet("Update", () =>
            {

            });

            RuleSet("Select", () =>
            {

            });
        }
    }
}
