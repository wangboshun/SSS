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
                RuleFor(x => x.jobname).NotNull().WithMessage("Job名称不能为空!");
                RuleFor(x => x.jobgroup).NotNull().WithMessage("Job组名称不能为空!");
                RuleFor(x => x.jobcron).NotNull().WithMessage("Cron策略不能为空!");
            });

            RuleSet("Delete", () =>
            {
                RuleFor(x => x.jobname).NotNull().WithMessage("Job名称不能为空!");
                RuleFor(x => x.jobgroup).NotNull().WithMessage("Job组名称不能为空!");
            });

            RuleSet("Update", () =>
            {
                RuleFor(x => x.jobname).NotNull().WithMessage("Job名称不能为空!");
                RuleFor(x => x.jobgroup).NotNull().WithMessage("Job组名称不能为空!");
            });

            RuleSet("Select", () =>
            {
                RuleFor(x => x.jobname).NotNull().WithMessage("Job名称不能为空!");
                RuleFor(x => x.jobgroup).NotNull().WithMessage("Job组名称不能为空!");
            });
        }
    }
}
