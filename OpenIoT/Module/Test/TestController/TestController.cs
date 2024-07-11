using Common.Utils;

using Furion.DynamicApiController;

using Microsoft.AspNetCore.Mvc;

using TestApplication;

using TestEntity.Dto;

namespace TestController
{
    [ApiDescriptionSettings("IoT", Tag = "Test")]
    [Route("iot/test")]
    public class TestController : IDynamicApiController
    {
        private readonly TestService _testService;

        public TestController(TestService testService)
        {
            _testService = testService;
        }

        /// <summary>
        /// 分页获取数据
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpGet("page")]
        public object GetPage([FromQuery] TestQueryPageDto input)
        {
            return ResponseUtils.Ok(_testService.GetPage(input));
        }
    }
}
