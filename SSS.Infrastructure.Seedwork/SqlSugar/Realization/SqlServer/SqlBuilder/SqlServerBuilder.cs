namespace SqlSugar
{
    public class SqlServerBuilder : SqlBuilderProvider
    {
        public override string SqlTranslationLeft { get { return "["; } }
        public override string SqlTranslationRight { get { return "]"; } }
    }
}
