using System;
using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Student.Dto
{
    public class StudentOutputDto : OutputDtoBase
    {
        public string name { set; get; }

        public int age { set; get; }

        public DateTime createtime { set; get; }
    }
}
