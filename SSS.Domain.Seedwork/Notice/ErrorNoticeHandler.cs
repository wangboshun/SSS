using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.Seedwork.Notice
{
    [DIService(ServiceLifetime.Scoped, typeof(ErrorNoticeHandler))]
    public class ErrorNoticeHandler
    {
        private List<ErrorNotice> _notice;

        public ErrorNoticeHandler()
        {
            _notice = new List<ErrorNotice>();
        }

        public Task Execute(ErrorNotice message, CancellationToken cancellationToken)
        {
            _notice.Add(message);

            return Task.CompletedTask;
        }

        public virtual List<ErrorNotice> GetNotice()
        {
            return _notice;
        }

        public virtual bool HasNotice()
        {
            return GetNotice().Any();
        }

        public void Dispose()
        {
            _notice = new List<ErrorNotice>();
        }
    }
}
