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
                RuleFor(x => x.jobname).NotNull().WithMessage("Job���Ʋ���Ϊ��!");
                RuleFor(x => x.jobgroup).NotNull().WithMessage("Job�����Ʋ���Ϊ��!");
                RuleFor(x => x.jobcron).NotNull().WithMessage("Cron���Բ���Ϊ��!");
            });

            RuleSet("Delete", () =>
            {
                RuleFor(x => x.jobname).NotNull().WithMessage("Job���Ʋ���Ϊ��!");
                RuleFor(x => x.jobgroup).NotNull().WithMessage("Job�����Ʋ���Ϊ��!");
            });

            RuleSet("Update", () =>
            {
                RuleFor(x => x.jobname).NotNull().WithMessage("Job���Ʋ���Ϊ��!");
                RuleFor(x => x.jobgroup).NotNull().WithMessage("Job�����Ʋ���Ϊ��!");
            });

            RuleSet("Select", () =>
            {
                RuleFor(x => x.jobname).NotNull().WithMessage("Job���Ʋ���Ϊ��!");
                RuleFor(x => x.jobgroup).NotNull().WithMessage("Job�����Ʋ���Ϊ��!");
            });
        }
    }
}
