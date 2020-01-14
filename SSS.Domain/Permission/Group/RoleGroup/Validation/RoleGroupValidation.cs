using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Group.RoleGroup.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RoleGroupInputDto>))]
    public class RoleGroupAddValidation : AbstractValidator<RoleGroupInputDto>
    {
        public RoleGroupAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.rolegroupname).NotNull().WithMessage("��ɫ�����Ʋ���Ϊ��!");
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
