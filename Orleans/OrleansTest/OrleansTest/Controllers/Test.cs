using Microsoft.AspNetCore.Mvc;

namespace OrleansTest.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class Test : ControllerBase
    {
        private readonly ILogger<Test> _logger;

        public Test(ILogger<Test> logger)
        {
            _logger = logger;
        }

        [HttpGet(Name = "test1")]
        public string Test1()
        {
            return "ok";
        }
    }
}
