using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinAnalyse.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Coin.CoinAnalyse.Service
{
    public interface ICoinAnalyseService : IQueryService<Domain.Coin.CoinAnalyse.CoinAnalyse,CoinAnalyseInputDto, CoinAnalyseOutputDto>
    {
        CoinAnalyseOutputDto AddCoinAnalyse(CoinAnalyseInputDto input);

        Pages<List<CoinAnalyseOutputDto>> GetListCoinAnalyse(CoinAnalyseInputDto input);
    }
}