using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.System.Job.JobInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.System.Job.JobInfo.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<JobInfoInputDto>))]
    public class JobInfoAddValidation : AbstractValidator<JobInfoInputDto>
    {
        public JobInfoAddValidation()
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
