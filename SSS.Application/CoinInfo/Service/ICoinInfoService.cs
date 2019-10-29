using SSS.Domain.CoinInfo.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;
using SSS.Application.Seedwork.Service;

namespace SSS.Application.CoinInfo.Service
{
    public interface ICoinInfoService : IQueryService<SSS.Domain.CoinInfo.CoinInfo, CoinInfoInputDto, CoinInfoOutputDto>
    {
        void AddCoinInfo(CoinInfoInputDto input);

		Pages<List<CoinInfoOutputDto>> GetListCoinInfo(CoinInfoInputDto input);
    }
}