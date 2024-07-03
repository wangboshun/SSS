using System.Text.Json.Serialization;

using Newtonsoft.Json;

namespace Common.Page;

public class Page<T>
{
    [JsonPropertyName("page_index")]
    [JsonProperty("page_index")]
    public int PageIndex { set; get; }

    [JsonPropertyName("page_size")]
    [JsonProperty("page_size")]
    public int PageSize { set; get; }

    [JsonPropertyName("count")]
    [JsonProperty("count")]
    public long Count { set; get; }

    [JsonPropertyName("rows")]
    [JsonProperty("rows")]
    public List<T> Rows { set; get; }
}