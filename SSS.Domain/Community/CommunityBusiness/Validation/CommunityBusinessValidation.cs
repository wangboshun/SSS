using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Community.CommunityBusiness.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Community.CommunityBusiness.Validation
{
    [DIService(ServiceLifetime.Singleton, typeof(IValidator<CommunityBusinessInputDto>))]
    public class CommunityBusinessAddValidation : AbstractValidator<CommunityBusinessInputDto>
    {
        public CommunityBusinessAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.name).NotNull().WithMessage("ÇëÌîÐ´ÒµÎñÃû³Æ");
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
