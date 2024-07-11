using Common.Database;

using FreeSql.DataAnnotations;

namespace ProduceEntity;

public class ProduceEntity : EntityBase
{
    /// <summary>
    /// 产品名称
    /// </summary>
	[Column(Name = "name")]
    public required string ProduceName { set; get; }

    /// <summary>
    /// 设备类型 直连设备、网关设备、网关子设备
    /// </summary>
   	[Column(Name = "device_type")]
    public required string DeviceType { set; get; }

    /// <summary>
    /// 存储类型 None、MySQL、MSSQL、ClickHouse、PGSQL
    /// </summary>
    [Column(Name = "store_type")]
    public required string StoreType { set; get; }

    /// <summary>
    /// 网络通信方式  TCP、UDP、MQTT、HTTP
    /// </summary>
    [Column(Name = "net_type")]
    public required string NetType { set; get; }

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
