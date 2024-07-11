using Common.Database;

using FreeSql.DataAnnotations;

namespace GatewayEntity;

public class GatewayEntity : EntityBase
{
    /// <summary>
    /// 网关服务名称
    /// </summary>
    [Column(Name = "name")]
    public required string GatewayName { set; get; }

    /// <summary>
    /// 类型 TCP服务、HTTP服务、UDP服务、MQTT服务
    /// </summary>
    [Column(Name = "type")]
    public required string Type { set; get; }

    /// <summary>
    /// 主机 0.0.0.0、127.0.0.1、192.168.1.1
    /// </summary>
    [Column(Name = "host")]
    public required string Host { set; get; }

    /// <summary>
    /// 端口
    /// </summary>
    [Column(Name = "port")]
    public required int Port { set; get; }

    /// <summary>
    /// 状态
    /// </summary>
    [Column(Name = "status")]
    public required int Status { set; get; }
}
