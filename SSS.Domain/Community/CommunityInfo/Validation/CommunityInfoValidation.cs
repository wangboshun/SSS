using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Community.CommunityInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Community.CommunityInfo.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<CommunityInfoInputDto>))]
    public class CommunityInfoAddValidation : AbstractValidator<CommunityInfoInputDto>
    {
        public CommunityInfoAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.name).NotNull().WithMessage("����д��������");
            });

            RuleSet("Delete", () =>
            {

            });

            RuleSet("Update", () =>
            {
                RuleFor(x => x.name).NotNull().WithMessage("����д��������");
            });

            RuleSet("Select", () =>
            {

            });
        }
    }
}
