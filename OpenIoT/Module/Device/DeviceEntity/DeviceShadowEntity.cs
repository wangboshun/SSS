using System.Text.Json.Serialization;

using FreeSql.DataAnnotations;

using Newtonsoft.Json;

namespace DeviceEntity;

/// <summary>
/// 设备影子
/// </summary>
public class DeviceShadowEntity
{
    /// <summary>
    /// 设备ID
    /// </summary>
    [Column(Name = "device_id")]
    [JsonProperty("device_id")]
    [JsonPropertyName("device_id")]
    public string DeviceId { set; get; }

    /// <summary>
    /// IP
    /// </summary>
    [Column(Name = "ip")]
    [JsonProperty("ip")]
    [JsonPropertyName("ip")]
    public string IP { set; get; }

    [Column(Name = "msg_type")]
    [JsonProperty("msg_type")]
    [JsonPropertyName("msg_type")]
    public string MsgType { set; get; }

    [Column(Name = "content")]
    [JsonProperty("content")]
    [JsonPropertyName("content")]
    public string Content { set; get; }

    /// <summary>
    ///     创建时间
    /// </summary>
    [Column(Name = "ct")]
    [JsonProperty("ct")]
    [JsonPropertyName("ct")]
    public DateTime CreateTime { set; get; }
}
