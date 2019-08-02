using SSS.Domain.Seedwork.Model;
using SSS.Domain.UserConfig.Dto;
using System.Collections.Generic;

namespace SSS.Application.UserConfig.Service
{
    public interface IUserConfigService
    {
        void AddUserConfig(UserConfigInputDto input);

        void UpdateUserConfig(UserConfigInputDto input);

        Pages<List<UserConfigOutputDto>> GetListUserConfig(UserConfigInputDto input);

        UserConfigOutputDto GetConfig(UserConfigInputDto input);
    }
}