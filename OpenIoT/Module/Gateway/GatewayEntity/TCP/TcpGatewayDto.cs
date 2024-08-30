using System.Text.Json.Serialization;
using Newtonsoft.Json;

namespace GatewayEntity.TCP
{
    public class TcpGatewayStartDto
    {
        [JsonProperty("id")]
        [JsonPropertyName("id")]
        public required string Id { set; get; }
        
        [JsonProperty("host")]
        [JsonPropertyName("host")]
        public required string Host { set; get; }

        [JsonProperty("port")]
        [JsonPropertyName("port")]
        public required int Port { set; get; }
    }
    
    public class TcpGatewayKOInputDto
    {
        [JsonProperty("server_id")]
        [JsonPropertyName("server_id")]
        public required string ServerId { set; get; }
        
        [JsonProperty("client_id")]
        [JsonPropertyName("client_id")]
        public required string ClientId { set; get; } 
    }
}
