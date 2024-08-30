using System.Text.Json.Serialization;
using Newtonsoft.Json;

namespace GatewayEntity.TCP;

public class TcpClientOutDto
{
    /// <summary>
    /// 客户端id
    /// </summary>
    [JsonProperty("client_id")]
    [JsonPropertyName("client_id")]
    public string ClientId { set; get; }
    
    /// <summary>
    /// 连接ip
    /// </summary>
    [JsonProperty("ip")]
    [JsonPropertyName("ip")]
    public string IP { set; get; }
    
    /// <summary>
    /// 连接时间
    /// </summary>
    [JsonProperty("connect_time")]
    [JsonPropertyName("connect_time")]
    public DateTime? ConnectTime { set; get; }
    
    /// <summary>
    /// 最后活动时间
    /// </summary>
    [JsonProperty("last_time")]
    [JsonPropertyName("last_time")]
    public DateTime? LastTime { set; get; }
    
    /// <summary>
    /// 接收数据大小
    /// </summary>
    [JsonProperty("receive_size")]
    [JsonPropertyName("receive_size")]
    public long? ReceiveSize { set; get; }
    
    /// <summary>
    /// 接收数据次数（不包含心跳包）
    /// </summary>
    [JsonProperty("receive_msg_count")]
    [JsonPropertyName("receive_msg_count")]
    public long? ReceiveMessageCount { set; get; } 
}