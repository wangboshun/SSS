using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Permission.Info.OperateInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Info.OperateInfo.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<OperateInfoInputDto>))]
    public class OperateInfoAddValidation : AbstractValidator<OperateInfoInputDto>
    {
        public OperateInfoAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.operatename).NotEmpty().WithMessage("�������Ʋ���Ϊ�գ�");
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
