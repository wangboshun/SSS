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
}
