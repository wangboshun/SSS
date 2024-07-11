using Common.Database;

using FreeSql.DataAnnotations;

namespace DeviceEntity;

public class DeviceEntity : EntityBase
{
    /// <summary>
    /// 设备名称
    /// </summary>
	[Column(Name = "name")]
    public required string DeviceName { set; get; }

    /// <summary>
    /// 产品ID
    /// </summary>
    [Column(Name = "produce_id")]
    public required string ProduceId { set; get; } 

    /// <summary>
    /// 状态
    /// </summary>
    [Column(Name = "status")]
    public required int Status { set; get; }

    /// <summary>
    /// 备注
    /// </summary>
    [Column(Name = "desc")]
    public string? Describe { set; get; }
}
