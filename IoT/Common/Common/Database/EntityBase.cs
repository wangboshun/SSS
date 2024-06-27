using System.Text.Json.Serialization;

using FreeSql.DataAnnotations;

using Furion.DistributedIDGenerator;

using Newtonsoft.Json;

namespace Common.Database;

public class EntityBase
{
    /// <summary>
    ///     Id
    /// </summary>
    [Column(IsPrimary = true, Name = "id", CanUpdate = false)]
    public string Id { set; get; } = ShortIDGen.NextID();

    /// <summary>
    ///     创建时间
    /// </summary>
    [Column(Name = "ct", ServerTime = DateTimeKind.Local, CanUpdate = false)]
    [JsonProperty("ct")]
    [JsonPropertyName("ct")]
    public DateTime CreateTime { set; get; }

    /// <summary>
    ///     更新时间
    /// </summary>
    [Column(Name = "ut")]
    [JsonProperty("ut")]
    [JsonPropertyName("ut")]
    public DateTime? UpdateTime { set; get; }
}