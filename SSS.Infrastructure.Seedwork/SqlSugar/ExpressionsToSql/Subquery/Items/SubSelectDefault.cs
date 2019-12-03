using System.Linq.Expressions;

namespace SqlSugar
{
    public class SubSelectDefault : ISubOperation
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
                return "SelectDefault";
            }
        }

        public int Sort
        {
            get
            {
                return 250;
            }
        }

        public string GetValue(Expression expression)
        {
            return "*";
        }
    }
}
