using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using SSS.Api.Seedwork;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.IO;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Runtime.InteropServices;
using System.Text;

namespace SSS.Api.Controllers
{
    [ApiVersion("1.0")]
    [Route("api/v{version:apiVersion}/[controller]")]
    [ApiController]
    public class CodeController : ApiBaseController
    {
        private IHostingEnvironment _env;

        private static string current_path;

        public CodeController(IHostingEnvironment env)
        {
            _env = env;
            current_path = _env.ContentRootPath;
        }

        [HttpGet("index")]
        public ContentResult Index()
        {
            string html = "";
            string filepath = RuntimeInformation.IsOSPlatform(OSPlatform.Linux) ? current_path + "//codegenerator.html" : current_path + "\\codegenerator.html";

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

        [HttpPost("createcode")]
        public IActionResult CreateCode()
        {
            var class_name = HttpContext.Request.Form["class_name"];
            //Generator_Domain(class_name);
            //Generator_Infrastructure(class_name);
            //Generator_CQRS(class_name);
            //Generator_Application(class_name);
            //Generator_Api(class_name);

            var fields = HttpContext.Request.Form["fields"];
            var list = JsonConvert.DeserializeObject<List<Field>>(fields);
            list = list.Where(x => !string.IsNullOrWhiteSpace(x.field_name)).ToList();
            AppDomainContext(class_name, list);

            return Response(null);
        }

        /// <summary>
        /// 填充类字段信息
        /// </summary>
        /// <param name="fields"></param>
        private void AppDomainContext(string name, List<Field> fields)
        {
            Directory.SetCurrentDirectory(Directory.GetParent(current_path).FullName);
            var parent_path = Directory.GetCurrentDirectory();

            var Class_Path = parent_path + $"\\SSS.Domain\\{name}\\{name}.cs";

            StringBuilder str = new StringBuilder();
            foreach (var item in fields)
            {
                string content = "        public " + item.field_type + " " + System.Threading.Thread.CurrentThread.CurrentCulture.TextInfo.ToTitleCase(item.field_name) + " { set; get; }";
                str.Append("\r\n\r\n" + content);
            }

            string Class_Content = IO.ReadAllText(Class_Path);
            int position = Class_Content.LastIndexOf("}") - 8;
            Class_Content = Class_Content.Insert(position, str.ToString());
            IO.Save(Class_Path, Class_Content);
        }

        /// <summary>
        /// 1
        /// </summary>
        /// <param name="name"></param>
        private void Generator_Domain(string name)
        {
            var TemplateInputDto_Read_Path = current_path + "\\Template\\Template_Domain\\Dto\\TemplateInputDto.txt";
            var TemplateOutputDto_Read_Path = current_path + "\\Template\\Template_Domain\\Dto\\TemplateOutputDto.txt";
            var Template_Read_Path = current_path + "\\Template\\Template_Domain\\Template.txt";

            string TemplateInputDto_Content = IO.ReadAllText(TemplateInputDto_Read_Path);
            TemplateInputDto_Content = TemplateInputDto_Content.Replace("Template", name);

            string TemplateOutputDto_Content = IO.ReadAllText(TemplateOutputDto_Read_Path);
            TemplateOutputDto_Content = TemplateOutputDto_Content.Replace("Template", name);

            string Template_Content = IO.ReadAllText(Template_Read_Path);
            Template_Content = Template_Content.Replace("Template", name);

            Directory.SetCurrentDirectory(Directory.GetParent(current_path).FullName);
            var parent_path = Directory.GetCurrentDirectory();

            var TemplateInputDto_Write_Path = parent_path + $"\\SSS.Domain\\{name}\\Dto\\{name}InputDto.cs";
            var TemplateOutputDto_Write_Path = parent_path + $"\\SSS.Domain\\{name}\\Dto\\{name}OutputDto.cs";
            var Template_Write_Path = parent_path + $"\\SSS.Domain\\{name}\\{name}.cs";

            IO.Save(TemplateInputDto_Write_Path, TemplateInputDto_Content);
            IO.Save(TemplateOutputDto_Write_Path, TemplateOutputDto_Content);
            IO.Save(Template_Write_Path, Template_Content);
        }

        /// <summary>
        /// 2
        /// </summary>
        /// <param name="name"></param>
        private void Generator_Infrastructure(string name)
        {
            var ITemplateRepository_Read_Path = current_path + "\\Template\\Template_Infrastructure\\ITemplateRepository.txt";
            var TemplateRepository_Read_Path = current_path + "\\Template\\Template_Infrastructure\\TemplateRepository.txt";

            string ITemplateRepository_Content = IO.ReadAllText(ITemplateRepository_Read_Path);
            ITemplateRepository_Content = ITemplateRepository_Content.Replace("Template", name);

            string TemplateRepository_Content = IO.ReadAllText(TemplateRepository_Read_Path);
            TemplateRepository_Content = TemplateRepository_Content.Replace("Template", name);

            Directory.SetCurrentDirectory(Directory.GetParent(current_path).FullName);
            var parent_path = Directory.GetCurrentDirectory();

            var ITemplateRepository_Write_Path = parent_path + $"\\SSS.Infrastructure\\Repository\\{name}\\I{name}Repository.cs";
            var TemplateRepository_Write_Path = parent_path + $"\\SSS.Infrastructure\\Repository\\{name}\\{name}Repository.cs";

            IO.Save(ITemplateRepository_Write_Path, ITemplateRepository_Content);
            IO.Save(TemplateRepository_Write_Path, TemplateRepository_Content);
        }

        /// <summary>
        /// 3
        /// </summary>
        /// <param name="name"></param>
        private void Generator_CQRS(string name)
        {
            var TemplateAddCommand_Read_Path = current_path + "\\Template\\Template_CQRS\\Command\\Commands\\TemplateAddCommand.txt";
            var TemplateCommand_Read_Path = current_path + "\\Template\\Template_CQRS\\Command\\Commands\\TemplateCommand.txt";
            var TemplateCommandHandler_Read_Path = current_path + "\\Template\\Template_CQRS\\Command\\Handlers\\TemplateCommandHandler.txt";
            var TemplateAddEvent_Read_Path = current_path + "\\Template\\Template_CQRS\\Event\\Events\\TemplateAddEvent.txt";
            var TemplateAddEventHandler_Read_Path = current_path + "\\Template\\Template_CQRS\\Event\\Handlers\\TemplateAddEventHandler.txt";
            var TemplateAddValidation_Read_Path = current_path + "\\Template\\Template_CQRS\\Validations\\TemplateAddValidation.txt";
            var TemplateValidation_Read_Path = current_path + "\\Template\\Template_CQRS\\Validations\\TemplateValidation.txt";


            string TemplateAddCommand_Content = IO.ReadAllText(TemplateAddCommand_Read_Path);
            TemplateAddCommand_Content = TemplateAddCommand_Content.Replace("Template", name);

            string TemplateCommand_Content = IO.ReadAllText(TemplateCommand_Read_Path);
            TemplateCommand_Content = TemplateCommand_Content.Replace("Template", name);

            string TemplateCommandHandler_Content = IO.ReadAllText(TemplateCommandHandler_Read_Path);
            TemplateCommandHandler_Content = TemplateCommandHandler_Content.Replace("Template", name);

            string TemplateAddEvent_Content = IO.ReadAllText(TemplateAddEvent_Read_Path);
            TemplateAddEvent_Content = TemplateAddEvent_Content.Replace("Template", name);

            string TemplateAddEventHandler_Content = IO.ReadAllText(TemplateAddEventHandler_Read_Path);
            TemplateAddEventHandler_Content = TemplateAddEventHandler_Content.Replace("Template", name);

            string TemplateAddValidation_Content = IO.ReadAllText(TemplateAddValidation_Read_Path);
            TemplateAddValidation_Content = TemplateAddValidation_Content.Replace("Template", name);

            string TemplateValidation_Content = IO.ReadAllText(TemplateValidation_Read_Path);
            TemplateValidation_Content = TemplateValidation_Content.Replace("Template", name);

            Directory.SetCurrentDirectory(Directory.GetParent(current_path).FullName);
            var parent_path = Directory.GetCurrentDirectory();

            var TemplateAddCommand_Write_Path = parent_path + $"\\SSS.Domain.CQRS\\{name}\\Command\\Commands\\{name}AddCommand.cs";
            var TemplateCommand_Write_Path = parent_path + $"\\SSS.Domain.CQRS\\{name}\\Command\\Commands\\{name}Command.cs";
            var TemplateCommandHandler_Write_Path = parent_path + $"\\SSS.Domain.CQRS\\{name}\\Command\\Handlers\\{name}CommandHandler.cs";
            var TemplateAddEvent_Write_Path = parent_path + $"\\SSS.Domain.CQRS\\{name}\\Event\\Events\\{name}AddEvent.cs";
            var TemplateAddEventHandler_Write_Path = parent_path + $"\\SSS.Domain.CQRS\\{name}\\Event\\Handlers\\{name}AddEventHandler.cs";
            var TemplateAddValidation_Write_Path = parent_path + $"\\SSS.Domain.CQRS\\{name}\\Validations\\{name}AddValidation.cs";
            var TemplateValidation_Write_Path = parent_path + $"\\SSS.Domain.CQRS\\{name}\\Validations\\{name}Validation.cs";

            IO.Save(TemplateAddCommand_Write_Path, TemplateAddCommand_Content);
            IO.Save(TemplateCommand_Write_Path, TemplateCommand_Content);
            IO.Save(TemplateCommandHandler_Write_Path, TemplateCommandHandler_Content);
            IO.Save(TemplateAddEvent_Write_Path, TemplateAddEvent_Content);
            IO.Save(TemplateAddEventHandler_Write_Path, TemplateAddEventHandler_Content);
            IO.Save(TemplateAddValidation_Write_Path, TemplateAddValidation_Content);
            IO.Save(TemplateValidation_Write_Path, TemplateValidation_Content);
        }

        /// <summary>
        /// 4
        /// </summary>
        /// <param name="name"></param>
        private void Generator_Application(string name)
        {
            var TemplateProfile_Read_Path = current_path + "\\Template\\Template_Application\\Mapper\\TemplateMapper.txt";
            var ITemplateService_Read_Path = current_path + "\\Template\\Template_Application\\Service\\ITemplateService.txt";
            var TemplateService_Read_Path = current_path + "\\Template\\Template_Application\\Service\\TemplateService.txt";

            string TemplateProfile_Content = IO.ReadAllText(TemplateProfile_Read_Path);
            TemplateProfile_Content = TemplateProfile_Content.Replace("Template", name);

            string ITemplateService_Content = IO.ReadAllText(ITemplateService_Read_Path);
            ITemplateService_Content = ITemplateService_Content.Replace("Template", name);

            string TemplateService_Content = IO.ReadAllText(TemplateService_Read_Path);
            TemplateService_Content = TemplateService_Content.Replace("Template", name);

            Directory.SetCurrentDirectory(Directory.GetParent(current_path).FullName);
            var parent_path = Directory.GetCurrentDirectory();

            var TemplateProfile_Write_Path = parent_path + $"\\SSS.Application\\{name}\\Mapper\\{name}Mapper.cs";
            var ITemplateService_Write_Path = parent_path + $"\\SSS.Application\\{name}\\Service\\I{name}Service.cs";
            var TemplateService_Write_Path = parent_path + $"\\SSS.Application\\{name}\\Service\\{name}Service.cs";

            IO.Save(TemplateProfile_Write_Path, TemplateProfile_Content);
            IO.Save(ITemplateService_Write_Path, ITemplateService_Content);
            IO.Save(TemplateService_Write_Path, TemplateService_Content);
        }

        /// <summary>
        /// 5
        /// </summary>
        /// <param name="name"></param>
        private void Generator_Api(string name)
        {
            var TemplateController_Read_Path = current_path + "\\Template\\Template_Api\\TemplateController.txt";
            var TemplateController_Write_Path = current_path + $"\\Controllers\\{name}Controller.cs";

            string TemplateController_Content = IO.ReadAllText(TemplateController_Read_Path);
            TemplateController_Content = TemplateController_Content.Replace("Template", name);

            IO.Save(TemplateController_Write_Path, TemplateController_Content);
        }
    }

    public class Field
    {
        public string field_name { set; get; }
        public string field_type { set; get; }
    }
}