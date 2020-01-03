using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;

using SSS.Api.Seedwork.Controller;
using SSS.Application.System.Generator;

using System.IO;
using System.Net;
using System.Runtime.InteropServices;

namespace SSS.Api.Controllers.System.Code
{
    /// <summary>	
    /// 代码生成	
    /// </summary>	
    [ApiVersion("3.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [ApiController]
    public class CodeController : ApiBaseController
    {
        private static string current_path;
        private readonly IWebHostEnvironment _env;
        private readonly IGeneratorCodeService _generatorCodeService;

        /// <summary>
        /// CodeController
        /// </summary>
        /// <param name="env"></param>
        /// <param name="generatorCodeService"></param>
        public CodeController(IWebHostEnvironment env, IGeneratorCodeService generatorCodeService)
        {
            _env = env;
            current_path = _env.ContentRootPath;
            _generatorCodeService = generatorCodeService;
        }

        /// <summary>	
        /// 主页	
        /// </summary>	
        /// <returns></returns>	
        [HttpGet("index")]
        public ContentResult Index()
        {
            string html = "";
            string filepath = RuntimeInformation.IsOSPlatform(OSPlatform.Linux)
                ? current_path + "//codegenerator.html"
                : current_path + "\\codegenerator.html";

            using (StreamReader sr = new StreamReader(filepath))
            {
                html = sr.ReadToEnd();
            }

            return new ContentResult
            {
                ContentType = "text/html",
                StatusCode = (int)HttpStatusCode.OK,
                Content = html
            };
        }

        /// <summary>	
        /// 生成操作	
        /// </summary>	
        /// <returns></returns>	
        [HttpPost("createcode")]
        public IActionResult CreateCode()
        {
            var result = _generatorCodeService.CreateCode(HttpContext.Request.Form["class_name"],
                HttpContext.Request.Form["namespace_name"], HttpContext.Request.Form["fields"]);
            return ApiResponse(result);
        }
    }
}