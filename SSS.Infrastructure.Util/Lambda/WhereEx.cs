using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;

namespace SSS.Infrastructure.Util.Lambda
{
    public class WhereEx
    {
        public string Key { get; set; } //过滤的关键字  
        public string Value { get; set; } //过滤的值  
        public string Contract { get; set; } // 过滤的约束 比如：'<' '<=' '>' '>=' 'like'等  
    }

    public static class DynamicLinq
    {
        public static IQueryable<T> Where<T>(this IQueryable<T> query, List<WhereEx> filters)
        {
            var param = CreateLambdaParam<T>("c");
            Expression body = Expression.Constant(true); //初始默认一个true  
            foreach (var filter in filters) body = body.AndAlso(param.GenerateBody<T>(filter)); //这里可以根据需要自由组合  
            var lambda = param.GenerateTypeLambda<T>(body); //最终组成lambda  
            return query.Where(lambda);
        }

        /// <summary>
        ///     创建lambda中的参数,即c=>c.xxx==xx 中的c
        /// </summary>
        public static ParameterExpression CreateLambdaParam<T>(string name)
        {
            return Expression.Parameter(typeof(T), name);
        }

        /// <summary>
        ///     创建linq表达示的body部分,即c=>c.xxx==xx 中的c.xxx==xx
        /// </summary>
        public static Expression GenerateBody<T>(this ParameterExpression param, WhereEx filterObj)
        {
            var property = typeof(T).GetProperty(filterObj.Key);

            //组装左边  
            Expression left = Expression.Property(param, property);
            //组装右边  
            Expression right = null;

            //todo: 下面根据需要，扩展自己的类型  
            if (property.PropertyType == typeof(int))
                right = Expression.Constant(int.Parse(filterObj.Value));
            else if (property.PropertyType == typeof(System.DateTime))
                right = Expression.Constant(Convert.ToDateTime(filterObj.Value));
            else if (property.PropertyType == typeof(string))
                right = Expression.Constant(filterObj.Value);
            else if (property.PropertyType == typeof(decimal))
                right = Expression.Constant(decimal.Parse(filterObj.Value));
            else if (property.PropertyType == typeof(Guid))
                right = Expression.Constant(Guid.Parse(filterObj.Value));
            else if (property.PropertyType == typeof(bool))
                right = Expression.Constant(filterObj.Value.Equals("1"));
            else
                throw new Exception("暂不能解析该Key的类型");

            //todo: 下面根据需要扩展自己的比较  
            Expression filter = Expression.Equal(left, right);
            switch (filterObj.Contract)
            {
                case "<=":
                    filter = Expression.LessThanOrEqual(left, right);
                    break;

                case "<":
                    filter = Expression.LessThan(left, right);
                    break;

                case ">":
                    filter = Expression.GreaterThan(left, right);
                    break;

                case ">=":
                    filter = Expression.GreaterThanOrEqual(left, right);
                    break;

                case "like":
                    filter = Expression.Call(left, typeof(string).GetMethod("Contains", new[] { typeof(string) }),
                        Expression.Constant(filterObj.Value));
                    break;
            }

            return filter;
        }

        /// <summary>
        ///     创建完整的lambda,即c=>c.xxx==xx
        /// </summary>
        public static LambdaExpression GenerateLambda(this ParameterExpression param, Expression body)
        {
            return Expression.Lambda(body, param);
        }

        /// <summary>
        ///     创建完整的lambda，为了兼容EF中的where语句
        /// </summary>
        public static Expression<Func<T, bool>> GenerateTypeLambda<T>(this ParameterExpression param, Expression body)
        {
            return (Expression<Func<T, bool>>)param.GenerateLambda(body);
        }

        public static Expression AndAlso(this Expression expression, Expression expressionRight)
        {
            return Expression.AndAlso(expression, expressionRight);
        }

        public static Expression Or(this Expression expression, Expression expressionRight)
        {
            return Expression.Or(expression, expressionRight);
        }

        public static Expression And(this Expression expression, Expression expressionRight)
        {
            return Expression.And(expression, expressionRight);
        }
    }
}