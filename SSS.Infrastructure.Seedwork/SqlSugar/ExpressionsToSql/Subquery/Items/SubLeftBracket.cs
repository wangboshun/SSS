using System.Linq.Expressions;

namespace SqlSugar
{
    public class SubLeftBracket : ISubOperation
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
                return "LeftBracket";
            }
        }

        public int Sort
        {
            get
            {
                return 50;
            }
        }

        public string GetValue(Expression expression)
        {
            return "(";
        }
    }
}
