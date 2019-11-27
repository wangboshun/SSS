using SSS.Domain.Permission.RoleOperate.Dto;

namespace SSS.Application.Permission.RoleOperate.Mapper
{
    public class RoleOperateProfile : AutoMapper.Profile
    {
        public RoleOperateProfile()
        {
            CreateMap<SSS.Domain.Permission.RoleOperate.RoleOperate, RoleOperateOutputDto>();

            CreateMap<RoleOperateInputDto, SSS.Domain.Permission.RoleOperate.RoleOperate>();
        }
    }
}
