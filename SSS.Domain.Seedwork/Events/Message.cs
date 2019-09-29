using MediatR;

namespace SSS.Domain.Seedwork.Events
{
    public class Message : IRequest<bool>
    {
        public string MsgType { set; get; }
        public string AggregateId { set; get; }
        protected Message()
        {
            MsgType = GetType().Name;
        }
    }
}
