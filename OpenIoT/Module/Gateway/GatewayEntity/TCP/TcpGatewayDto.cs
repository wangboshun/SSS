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
}
