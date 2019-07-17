using SSS.Domain.UserConfig.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;

namespace SSS.Application.UserConfig.Service
{
    public interface IUserConfigService
    {
        void AddUserConfig(UserConfigInputDto input);

		Pages<List<UserConfigOutputDto>> GetListUserConfig(UserConfigInputDto input);

        UserConfigOutputDto GetConfig(UserConfigInputDto input);
    }
}