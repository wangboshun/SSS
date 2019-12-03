using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;

using Newtonsoft.Json;

using SSS.Api.Seedwork.Controller;
using SSS.Infrastructure.Util.IO;

using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;

namespace SSS.Api.Controllers.System
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
        private static string namespace_name;
        private static string namespace_path;
        private static string class_name;

        /// <summary>	
        /// 构造	
        /// </summary>	
        /// <param name="env"></param>	
        public CodeController(IWebHostEnvironment env)
        {
            _env = env;
            current_path = _env.ContentRootPath;
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
            class_name = HttpContext.Request.Form["class_name"];
            if (string.IsNullOrWhiteSpace(class_name))
                return ApiResponse(null);

            string namespace_str = HttpContext.Request.Form["namespace_name"];
            if (!string.IsNullOrWhiteSpace(namespace_str))
            {
                namespace_name = namespace_str;

                if (namespace_str.Contains("."))
                {
                    string[] array = namespace_str.Split(".");
                    namespace_path = "\\" + array[0] + "\\" + array[1];
                }
                else
                    namespace_path = "\\" + namespace_str;
            }

            Generator_Domain();
            Generator_Infrastructure();
            Generator_Application();
            Generator_Api();

            var fields = HttpContext.Request.Form["fields"];
            var list = JsonConvert.DeserializeObject<List<Field>>(fields);
            list = list.Where(x => !string.IsNullOrWhiteSpace(x.field_name)).ToList();
            AppDomainContext(list);

            return ApiResponse(null);
        }

        /// <summary>	
        /// 填充类字段信息	
        /// </summary>	  
        /// <param name="fields"></param>	
        private void AppDomainContext(List<Field> fields)
        {
            Directory.SetCurrentDirectory(Directory.GetParent(current_path).FullName);
            var parent_path = Directory.GetCurrentDirectory();

            var Class_Path = parent_path + $"\\SSS.Domain{namespace_path}\\{class_name}\\{class_name}.cs";

            StringBuilder str = new StringBuilder();
            foreach (var item in fields)
            {
                string content = "        public " + item.field_type + " " +
                                 Thread.CurrentThread.CurrentCulture.TextInfo.ToTitleCase(item.field_name) +
                                 " { set; get; }";
                str.Append("\r\n\r\n" + content);
            }

            string Class_Content = IO.ReadAllText(Class_Path);
            int position = Class_Content.LastIndexOf("}") - 8;
            Class_Content = Class_Content.Insert(position, str.ToString());
            IO.Save(Class_Path, Class_Content);
        }

        /// <summary>	
        ///     1	
        /// </summary>	 
        private void Generator_Domain()
        {
            var TemplateInputDto_Read_Path = current_path + "\\Template\\Template_Domain\\Dto\\TemplateInputDto.txt";
            var TemplateOutputDto_Read_Path = current_path + "\\Template\\Template_Domain\\Dto\\TemplateOutputDto.txt";
            var Template_Read_Path = current_path + "\\Template\\Template_Domain\\Template.txt";
            var TemplateValidation_Read_Path = current_path + "\\Template\\Template_Domain\\Validation\\TemplateValidation.txt";

            string TemplateInputDto_Content = IO.ReadAllText(TemplateInputDto_Read_Path);
            TemplateInputDto_Content = TemplateInputDto_Content.Replace("Template", class_name);
            if (!string.IsNullOrWhiteSpace(namespace_name))
                TemplateInputDto_Content = TemplateInputDto_Content.Replace("NameSpace", namespace_name);
            else
                TemplateInputDto_Content = TemplateInputDto_Content.Replace(".NameSpace", "");


            string TemplateOutputDto_Content = IO.ReadAllText(TemplateOutputDto_Read_Path);
            TemplateOutputDto_Content = TemplateOutputDto_Content.Replace("Template", class_name);
            if (!string.IsNullOrWhiteSpace(namespace_name))
                TemplateOutputDto_Content = TemplateOutputDto_Content.Replace("NameSpace", namespace_name);
            else
                TemplateOutputDto_Content = TemplateOutputDto_Content.Replace(".NameSpace", "");


            string Template_Content = IO.ReadAllText(Template_Read_Path);
            Template_Content = Template_Content.Replace("Template", class_name);
            if (!string.IsNullOrWhiteSpace(namespace_name))
                Template_Content = Template_Content.Replace("NameSpace", namespace_name);
            else
                Template_Content = Template_Content.Replace(".NameSpace", "");


            string TemplateValidation_Content = IO.ReadAllText(TemplateValidation_Read_Path);
            TemplateValidation_Content = TemplateValidation_Content.Replace("Template", class_name);
            if (!string.IsNullOrWhiteSpace(namespace_name))
                TemplateValidation_Content = TemplateValidation_Content.Replace("NameSpace", namespace_name);
            else
                TemplateValidation_Content = TemplateValidation_Content.Replace(".NameSpace", "");

            Directory.SetCurrentDirectory(Directory.GetParent(current_path).FullName);
            var parent_path = Directory.GetCurrentDirectory();

            var TemplateInputDto_Write_Path = parent_path + $"\\SSS.Domain{namespace_path}\\{class_name}\\Dto\\{class_name}InputDto.cs";
            var TemplateOutputDto_Write_Path = parent_path + $"\\SSS.Domain{namespace_path}\\{class_name}\\Dto\\{class_name}OutputDto.cs";
            var Template_Write_Path = parent_path + $"\\SSS.Domain{namespace_path}\\{class_name}\\{class_name}.cs";
            var TemplateValidation_Write_Path = parent_path + $"\\SSS.Domain{namespace_path}\\{class_name}\\Validation\\{class_name}Validation.cs";

            IO.Save(TemplateInputDto_Write_Path, TemplateInputDto_Content);
            IO.Save(TemplateOutputDto_Write_Path, TemplateOutputDto_Content);
            IO.Save(Template_Write_Path, Template_Content);
            IO.Save(TemplateValidation_Write_Path, TemplateValidation_Content);
        }

        /// <summary>	
        ///     2	
        /// </summary>	 
        private void Generator_Infrastructure()
        {
            var ITemplateRepository_Read_Path =
                current_path + "\\Template\\Template_Infrastructure\\ITemplateRepository.txt";
            var TemplateRepository_Read_Path =
                current_path + "\\Template\\Template_Infrastructure\\TemplateRepository.txt";

            string ITemplateRepository_Content = IO.ReadAllText(ITemplateRepository_Read_Path);
            ITemplateRepository_Content = ITemplateRepository_Content.Replace("Template", class_name);
            if (!string.IsNullOrWhiteSpace(namespace_name))
                ITemplateRepository_Content = ITemplateRepository_Content.Replace("NameSpace", namespace_name);
            else
                ITemplateRepository_Content = ITemplateRepository_Content.Replace(".NameSpace", "");


            string TemplateRepository_Content = IO.ReadAllText(TemplateRepository_Read_Path);
            TemplateRepository_Content = TemplateRepository_Content.Replace("Template", class_name);
            if (!string.IsNullOrWhiteSpace(namespace_name))
                TemplateRepository_Content = TemplateRepository_Content.Replace("NameSpace", namespace_name);
            else
                TemplateRepository_Content = TemplateRepository_Content.Replace(".NameSpace", "");


            Directory.SetCurrentDirectory(Directory.GetParent(current_path).FullName);
            var parent_path = Directory.GetCurrentDirectory();

            var ITemplateRepository_Write_Path =
               parent_path + $"\\SSS.Infrastructure\\Repository{namespace_path}\\{class_name}\\I{class_name}Repository.cs";
            var TemplateRepository_Write_Path =
                parent_path + $"\\SSS.Infrastructure\\Repository{namespace_path}\\{class_name}\\{class_name}Repository.cs";

            IO.Save(ITemplateRepository_Write_Path, ITemplateRepository_Content);
            IO.Save(TemplateRepository_Write_Path, TemplateRepository_Content);
        }

        /// <summary>	
        ///     3	
        /// </summary>	 
        private void Generator_Application()
        {
            var TemplateProfile_Read_Path =
                current_path + "\\Template\\Template_Application\\Mapper\\TemplateMapper.txt";
            var ITemplateService_Read_Path =
                current_path + "\\Template\\Template_Application\\Service\\ITemplateService.txt";
            var TemplateService_Read_Path =
                current_path + "\\Template\\Template_Application\\Service\\TemplateService.txt";

            string TemplateProfile_Content = IO.ReadAllText(TemplateProfile_Read_Path);
            TemplateProfile_Content = TemplateProfile_Content.Replace("Template", class_name);
            if (!string.IsNullOrWhiteSpace(namespace_name))
                TemplateProfile_Content = TemplateProfile_Content.Replace("NameSpace", namespace_name);
            else
                TemplateProfile_Content = TemplateProfile_Content.Replace(".NameSpace", "");


            string ITemplateService_Content = IO.ReadAllText(ITemplateService_Read_Path);
            ITemplateService_Content = ITemplateService_Content.Replace("Template", class_name);
            if (!string.IsNullOrWhiteSpace(namespace_name))
                ITemplateService_Content = ITemplateService_Content.Replace("NameSpace", namespace_name);
            else
                ITemplateService_Content = ITemplateService_Content.Replace(".NameSpace", "");


            string TemplateService_Content = IO.ReadAllText(TemplateService_Read_Path);
            TemplateService_Content = TemplateService_Content.Replace("Template", class_name);
            if (!string.IsNullOrWhiteSpace(namespace_name))
                TemplateService_Content = TemplateService_Content.Replace("NameSpace", namespace_name);
            else
                TemplateService_Content = TemplateService_Content.Replace(".NameSpace", "");


            Directory.SetCurrentDirectory(Directory.GetParent(current_path).FullName);
            var parent_path = Directory.GetCurrentDirectory();

            var TemplateProfile_Write_Path = parent_path + $"\\SSS.Application{namespace_path}\\{class_name}\\Mapper\\{class_name}Mapper.cs";
            var ITemplateService_Write_Path = parent_path + $"\\SSS.Application{namespace_path}\\{class_name}\\Service\\I{class_name}Service.cs";
            var TemplateService_Write_Path = parent_path + $"\\SSS.Application{namespace_path}\\{class_name}\\Service\\{class_name}Service.cs";

            IO.Save(TemplateProfile_Write_Path, TemplateProfile_Content);
            IO.Save(ITemplateService_Write_Path, ITemplateService_Content);
            IO.Save(TemplateService_Write_Path, TemplateService_Content);
        }

        /// <summary>	
        ///     4	
        /// </summary>	 
        private void Generator_Api()
        {
            var TemplateController_Read_Path = current_path + "\\Template\\Template_Api\\TemplateController.txt";
            var TemplateController_Write_Path = current_path + $"\\Controllers{namespace_path}\\{class_name}Controller.cs";

            string TemplateController_Content = IO.ReadAllText(TemplateController_Read_Path);
            TemplateController_Content = TemplateController_Content.Replace("Template", class_name);
            if (!string.IsNullOrWhiteSpace(namespace_name))
                TemplateController_Content = TemplateController_Content.Replace("NameSpace", namespace_name);
            else
                TemplateController_Content = TemplateController_Content.Replace(".NameSpace", "");

            IO.Save(TemplateController_Write_Path, TemplateController_Content);
        }
    }

    /// <summary>	
    /// Field	
    /// </summary>	
    public class Field
    {
        /// <summary>	
        /// field_name	
        /// </summary>	
        public string field_name { set; get; }

        /// <summary>	
        /// field_type	
        /// </summary>	
        public string field_type { set; get; }
    }
}