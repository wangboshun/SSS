using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;
using System.Linq;

namespace SSS.Infrastructure.Repository.Student
{
    [DIService(ServiceLifetime.Scoped, typeof(IStudentRepository))]
    public class StudentRepository : Repository<SSS.Domain.Student.Student>, IStudentRepository
    {
        public SSS.Domain.Student.Student GetByName(string name)
        {
            return DbSet.AsNoTracking().FirstOrDefault(x => x.Name.Contains(name));
        }

        public StudentRepository(DbcontextBase context) : base(context)
        {
        }
    }
}