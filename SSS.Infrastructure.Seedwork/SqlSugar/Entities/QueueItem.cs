namespace SqlSugar
{
    public class QueueItem
    {
        public string Sql { get; set; }
        public SugarParameter[] Parameters { get; set; }
    }
}
