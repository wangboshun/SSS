namespace SqlSugar
{
    public partial class SqlServerExpressionContext : ExpressionContext, ILambdaExpressions
    {
        public SqlSugarProvider Context { get; set; }
        public SqlServerExpressionContext()
        {
            base.DbMehtods = new SqlServerMethod();
        }

    }
    public partial class SqlServerMethod : DefaultDbMethod, IDbMethods
    {
        public override string HasValue(MethodCallExpressionModel model)
        {
            if (model.Args[0].Type == UtilConstants.GuidType)
            {
                var parameter = model.Args[0];
                return string.Format("( {0} IS NOT NULL )", parameter.MemberName);
            }
            else
            {
                var parameter = model.Args[0];
                return string.Format("( {0}<>'' AND {0} IS NOT NULL )", parameter.MemberName);
            }
        }
    }
}
