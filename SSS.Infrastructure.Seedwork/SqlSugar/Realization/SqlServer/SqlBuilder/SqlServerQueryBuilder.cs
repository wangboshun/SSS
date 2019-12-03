namespace SqlSugar
{
    public class SqlServerQueryBuilder : QueryBuilder
    {
        public override string SqlTemplate
        {
            get
            {
                return "SELECT {0}{" + UtilConstants.ReplaceKey + "} FROM {1}{2}{3}{4}";
            }
        }
    }
}
