using System.Linq.Expressions;
namespace SqlSugar
{
    public class MemberNewExpressionResolve : BaseResolve
    {
        public MemberNewExpressionResolve(ExpressionParameter parameter) : base(parameter)
        {
            var expression = base.Expression as MemberExpression;
            var isLeft = parameter.IsLeft;
            object value = null;
            value = ExpressionTool.DynamicInvoke(expression);
        }
    }
}
