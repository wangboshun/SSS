using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Seedwork.UnitOfWork;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Seedwork.UnitOfWork
{
    [DIService(ServiceLifetime.Scoped, typeof(IUnitOfWork))]
    public class UnitOfWork : IUnitOfWork
    {
        private readonly DbcontextBase _context;

        public UnitOfWork(DbcontextBase context)
        {
            _context = context;
        }

        public bool Commit()
        {
            return _context.SaveChanges() > 0;
        }

        public void Dispose()
        {
            _context.Dispose();
        }
    }
}