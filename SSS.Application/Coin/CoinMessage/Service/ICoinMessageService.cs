using SSS.Application.Seedwork.Service;
using SSS.Domain.Coin.CoinMessage.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Coin.CoinMessage.Service
{
    public interface ICoinMessageService : IQueryService<Domain.Coin.CoinMessage.CoinMessage, CoinMessageInputDto,
        CoinMessageOutputDto>
    {
        void AddCoinMessage(CoinMessageInputDto input);

        Pages<List<CoinMessageOutputDto>> GetListCoinMessage(CoinMessageInputDto input);
    }
}