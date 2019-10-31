using SSS.Domain.CoinMessage.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;
using SSS.Application.Seedwork.Service;

namespace SSS.Application.CoinMessage.Service
{
    public interface ICoinMessageService : IQueryService<SSS.Domain.CoinMessage.CoinMessage, CoinMessageInputDto, CoinMessageOutputDto>
    {
        void AddCoinMessage(CoinMessageInputDto input);

		Pages<List<CoinMessageOutputDto>> GetListCoinMessage(CoinMessageInputDto input);
    }
}