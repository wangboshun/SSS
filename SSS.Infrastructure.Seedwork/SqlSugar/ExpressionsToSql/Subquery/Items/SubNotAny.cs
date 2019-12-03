using System.Linq.Expressions;

namespace SqlSugar
{
    public class SubNotAny : ISubOperation
    {
        public bool HasWhere
        {
            get; set;
        }

        public ExpressionContext Context
        {
            get; set;
        }

        public Expression Expression
        {
            get; set;
        }

        public string Name
        {
            get
            {
                return "NotAny";
            }
        }

        public int Sort
        {
            get
            {
                return 0;
            }
        }

        public string GetValue(Expression expression)
        {
            return "NOT EXISTS";
        }
    }
}
