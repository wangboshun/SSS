using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.IdentityModel.Tokens;

using SSS.Api.Seedwork.Controller;
using SSS.Application.Permission.Info.UserInfo.Service;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Infrastructure.Repository.Coin.CoinTrade;
using SSS.Infrastructure.Util.Config;
using SSS.Infrastructure.Util.DI;

using System;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace SSS.Api.Controllers.System
{
    [Route("api/v{version:apiVersion}/[controller]")]
    [ApiController]
    [ApiVersion("1.0")]
    [Produces("application/json")]
    public class AuthController : ApiBaseController
    {
        private readonly IUserInfoService _service;

        public AuthController(IUserInfoService service)
        {
            _service = service;
        }

        /// <summary>
        /// 授权
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [AllowAnonymous]
        [HttpPost("auth")]
        public IActionResult Auth([FromBody] UserInfoInputDto input)
        {
            var a = IocEx.Instance.GetService<ICoinTradeRepository>();

            var result = _service.GetByUserName(input);
            if (result == null)
                return ApiResponse(null, false, "登录失败", 204);

            var claims = new[]
               {
                    new Claim(JwtRegisteredClaimNames.Nbf,$"{new DateTimeOffset(DateTime.Now).ToUnixTimeSeconds()}") ,
                    new Claim (JwtRegisteredClaimNames.Exp,$"{new DateTimeOffset(DateTime.Now.AddMinutes(1)).ToUnixTimeSeconds()}"),
                    new Claim(ClaimTypes.Name, input.username)
                };

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(JsonConfig.GetSectionValue("Auth:SecurityKey")));
            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
            var token = new JwtSecurityToken(
                issuer: JsonConfig.GetSectionValue("Auth:Domain"),
                audience: JsonConfig.GetSectionValue("Auth:Domain"),
                claims: claims,
                expires: DateTime.Now.AddMinutes(1),
                signingCredentials: creds);

            return Ok(new { token = new JwtSecurityTokenHandler().WriteToken(token) });
        }
    }
}