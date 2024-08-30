using TouchSocket.Core;
using TouchSocket.Sockets;

namespace GatewayApplication.TCP;

public class DefaultTcpService  : TcpService<SocketClient>, ITcpService
{
    private readonly 
    /// <summary>
    /// 处理数据
    /// </summary>
    public ReceivedEventHandler<SocketClient> Received { get; set; }

    /// <inheritdoc/>
    protected override async Task OnReceived(SocketClient socketClient, ReceivedDataEventArgs e)
    {
        if (this.Received != null)
        {
            await this.Received.Invoke(socketClient, e).ConfigureFalseAwait();
            if (e.Handled)
            {
                return;
            }
        }

        await base.OnReceived(socketClient, e);
    }
}