using System.Text.Json.Serialization;

using FreeSql.DataAnnotations;

using Newtonsoft.Json;

namespace GatewayEntity
{
    public class ReportEntity
    {
        [Column(Name = "id")]
        [JsonProperty("id")]
        [JsonPropertyName("id")]
        public string Id { set; get; }

        [Column(Name = "produce_id")]
        [JsonProperty("produce_id")]
        [JsonPropertyName("produce_id")]
        public string ProduceId { set; get; }

        [Column(Name = "device_id")]
        [JsonProperty("device_id")]
        [JsonPropertyName("device_id")]
        public string DeviceId { set; get; }

        [Column(Name = "msg_type")]
        [JsonProperty("msg_type")]
        [JsonPropertyName("msg_type")]
        public string MsgType { set; get; } 

        [Column(Name = "tm")]
        [JsonProperty("tm")]
        [JsonPropertyName("tm")]
        public DateTime TM { set; get; }

        [Column(Name = "ct")]
        [JsonProperty("ct")]
        [JsonPropertyName("ct")]
        public DateTime CT { set; get; }

        [Column(Name = "content")]
        [JsonProperty("content")]
        [JsonPropertyName("content")]
        public string Content { set; get; }
    }
}
