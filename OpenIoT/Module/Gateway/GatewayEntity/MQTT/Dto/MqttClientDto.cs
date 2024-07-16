using System.Text.Json.Serialization;
using Newtonsoft.Json;

namespace GatewayEntity.MQTT.Dto;

public class MqttClientOutDto
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
    public DateTime ConnectTime { set; get; }
    
    /// <summary>
    /// 最后活动时间
    /// </summary>
    [JsonProperty("last_time")]
    [JsonPropertyName("last_time")]
    public DateTime LastTime { set; get; }
    
    /// <summary>
    /// 接收数据大小
    /// </summary>
    [JsonProperty("receive_size")]
    [JsonPropertyName("receive_size")]
    public long ReceiveSize { set; get; }
    
    /// <summary>
    /// 接收数据次数（包含消息和心跳包）
    /// </summary>
    [JsonProperty("receive_count")]
    [JsonPropertyName("receive_count")]
    public long ReceiveCount { set; get; }
    
    /// <summary>
    /// 接收数据次数（不包含消息和心跳包）
    /// </summary>
    [JsonProperty("receive_msg_count")]
    [JsonPropertyName("receive_msg_count")]
    public long ReceiveMessageCount { set; get; }
    
    /// <summary>
    /// 发送数据大小
    /// </summary>
    [JsonProperty("send_size")]
    [JsonPropertyName("send_size")]
    public long SendSize { set; get; }
    
    /// <summary>
    /// 发送数据次数（包含消息和心跳包）
    /// </summary>
    [JsonProperty("send_count")]
    [JsonPropertyName("send_count")]
    public long SendCount { set; get; }
    
    /// <summary>
    /// 发送消息数据次数（不包含消息和心跳包）
    /// </summary>
    [JsonProperty("send_msg_count")]
    [JsonPropertyName("send_msg_count")]
    public long SendMessageCount { set; get; }
}