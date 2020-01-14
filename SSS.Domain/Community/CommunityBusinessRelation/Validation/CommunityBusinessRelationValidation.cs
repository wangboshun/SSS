using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Community.CommunityBusinessRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Community.CommunityBusinessRelation.Validation
{
    [DIService(ServiceLifetime.Singleton, typeof(IValidator<CommunityBusinessRelationInputDto>))]
    public class CommunityBusinessRelationAddValidation : AbstractValidator<CommunityBusinessRelationInputDto>
    {
        public CommunityBusinessRelationAddValidation()
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
