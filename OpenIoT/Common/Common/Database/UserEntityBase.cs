using FreeSql.DataAnnotations;

using Newtonsoft.Json;

namespace Common.Database;

public class UserEntityBase : EntityBase
{
    /// <summary>
    ///     Id
    /// </summary>
    [Column(Name = "uid")]
    [JsonProperty("uid")]
    [JsonIgnore]
    public string UserId { set; get; }
}