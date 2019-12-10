using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Coin.CoinInfo.Service
{
    public interface
        ICoinInfoService : IQueryService<Domain.Coin.CoinInfo.CoinInfo, CoinInfoInputDto, CoinInfoOutputDto>
    {
        bool AddCoinInfo(CoinInfoInputDto input);

        Pages<List<CoinInfoOutputDto>> GetListCoinInfo(CoinInfoInputDto input);
    }
}