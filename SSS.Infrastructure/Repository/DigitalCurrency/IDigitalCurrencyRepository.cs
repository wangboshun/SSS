using System.Linq;
using SSS.Domain.DigitalCurrency.Dto;
using SSS.Domain.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.DigitalCurrency
{
    public interface IDigitalCurrencyRepository : IRepository<SSS.Domain.DigitalCurrency.DigitalCurrency>
    {
        IQueryable<Domain.DigitalCurrency.DigitalCurrency> GetPageOrderByAsc(DigitalCurrencyInputDto input,ref int count);
    }
}