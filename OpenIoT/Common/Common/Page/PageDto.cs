using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using System.Text.Json.Serialization;

namespace Common.Page;

public class PageDto
{
    [JsonPropertyName("page_index")]
    [JsonProperty("page_index")]
    [FromQuery(Name = "page_index")]
    public int PageIndex { set; get; } = 1;

    [JsonPropertyName("page_size")]
    [JsonProperty("page_size")]
    [FromQuery(Name = "page_size")]
    public int PageSize { set; get; } = 10;

    /// <summary>
    ///     ��ʼʱ��
    /// </summary>
    [FromQuery(Name = "start")]
    [JsonProperty("start")]
    [JsonPropertyName("start")]
    public DateTime StartTime { set; get; } = DateTime.Today;

    /// <summary>
    ///     ����ʱ��
    /// </summary>
    [FromQuery(Name = "end")]
    [JsonProperty("end")]
    [JsonPropertyName("end")]
    public DateTime EndTime { set; get; } = DateTime.Now;
}