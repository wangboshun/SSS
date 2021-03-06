﻿using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Hosting;

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
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [ApiController]
    public class CodeController : ApiBaseController
    {
        private static string current_path;
        private readonly IHostEnvironment _env;
        private readonly IGeneratorCodeService _generatorCodeService;

        /// <summary>
        /// 代码生成
        /// </summary>
        /// <param name="env"></param>
        /// <param name="generatorCodeService"></param>
        public CodeController(IHostEnvironment env, IGeneratorCodeService generatorCodeService)
        {
            _env = env;
            current_path = _env.ContentRootPath;
            _generatorCodeService = generatorCodeService;
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

        /// <summary>
        /// 主页
        /// </summary>
        /// <returns></returns>
        [HttpGet("index")]
        public ContentResult Index()
        {
            var html = "";
            var filepath = RuntimeInformation.IsOSPlatform(OSPlatform.Linux)
                ? current_path + "//codegenerator.html"
                : current_path + "\\codegenerator.html";

            using (var sr = new StreamReader(filepath))
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
    }
}