using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Community.CommunityTask.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Community.CommunityTask.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<CommunityTaskInputDto>))]
    public class CommunityTaskAddValidation : AbstractValidator<CommunityTaskInputDto>
    {
        public CommunityTaskAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.name).NotNull().WithMessage("请填写标题名称");
                RuleFor(x => x.contact).NotNull().WithMessage("请填写联系方式");
                RuleFor(x => x.businessid).NotNull().WithMessage("请填写任务类型");
                RuleFor(x => x.detail).NotNull().WithMessage("请填写任务介绍");
                RuleFor(x => x.userid).NotNull().WithMessage("创建人错误");
                RuleFor(x => x.email).NotNull().WithMessage("请填写邮箱");
                RuleFor(x => x.phone).NotNull().WithMessage("请填写手机号");
            });

            RuleSet("Delete", () => { });

            RuleSet("Update", () =>
            {
                RuleFor(x => x.name).NotNull().WithMessage("请填写标题名称");
                RuleFor(x => x.contact).NotNull().WithMessage("请填写联系方式");
                RuleFor(x => x.businessid).NotNull().WithMessage("请填写任务类型");
                RuleFor(x => x.detail).NotNull().WithMessage("请填写任务介绍");
                RuleFor(x => x.userid).NotNull().WithMessage("创建人错误");
                RuleFor(x => x.email).NotNull().WithMessage("请填写邮箱");
                RuleFor(x => x.phone).NotNull().WithMessage("请填写手机号");
            });

            RuleSet("Select", () => { });
        }
    }
}
