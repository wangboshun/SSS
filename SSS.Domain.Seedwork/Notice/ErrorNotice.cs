namespace SSS.Domain.Seedwork.Notice
{
    public class ErrorNotice
    {
        public string Key { get; set; }
        public string Value { get; set; }

        public ErrorNotice(string key, string value)
        {
            Key = key;
            Value = value;
        }
    }
}
