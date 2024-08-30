using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace GatewayEntity.MQTT.Dto
{
    public class MtttGatewayStartDto
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
    
    public class MqttGatewayKOInputDto
    {
        [JsonProperty("server_id")]
        [JsonPropertyName("server_id")]
        public required string ServerId { set; get; }
        
        [JsonProperty("client_id")]
        [JsonPropertyName("client_id")]
        public required string ClientId { set; get; } 
    }
}
