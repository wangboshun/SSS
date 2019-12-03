using System.Linq.Expressions;

namespace SqlSugar
{
    public interface ISubOperation
    {
        ExpressionContext Context { get; set; }
        string Name { get; }
        string GetValue(Expression expression);
        int Sort { get; }
        Expression Expression { get; set; }
        bool HasWhere { get; set; }
    }
}
