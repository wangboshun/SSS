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
                RuleFor(x => x.name).NotNull().WithMessage("����д��������");
                RuleFor(x => x.contact).NotNull().WithMessage("����д��ϵ��ʽ");
                RuleFor(x => x.businessid).NotNull().WithMessage("����д��������");
                RuleFor(x => x.detail).NotNull().WithMessage("����д�������");
                RuleFor(x => x.userid).NotNull().WithMessage("�����˴���");
                RuleFor(x => x.email).NotNull().WithMessage("����д����");
                RuleFor(x => x.phone).NotNull().WithMessage("����д�ֻ���");
            });

            RuleSet("Delete", () => { });

            RuleSet("Update", () =>
            {
                RuleFor(x => x.name).NotNull().WithMessage("����д��������");
                RuleFor(x => x.contact).NotNull().WithMessage("����д��ϵ��ʽ");
                RuleFor(x => x.businessid).NotNull().WithMessage("����д��������");
                RuleFor(x => x.detail).NotNull().WithMessage("����д�������");
                RuleFor(x => x.userid).NotNull().WithMessage("�����˴���");
                RuleFor(x => x.email).NotNull().WithMessage("����д����");
                RuleFor(x => x.phone).NotNull().WithMessage("����д�ֻ���");
            });

            RuleSet("Select", () => { });
        }
    }
}
