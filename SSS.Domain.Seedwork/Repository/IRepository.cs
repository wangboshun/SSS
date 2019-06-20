using System;
using System.Linq;
using System.Linq.Expressions;

namespace SSS.Domain.Seedwork.Repository
{
    public interface IRepository<TEntity> : IDisposable where TEntity : class
    {
        void Add(TEntity obj);
        TEntity Get(string id);
        TEntity Get(Expression<Func<TEntity, bool>> predicate);
        IQueryable<TEntity> GetBySql(string sql);
        IQueryable<TEntity> GetBySql(string sql, params object[] parameter);
        IQueryable<TEntity> GetAll();
        IQueryable<TEntity> GetAll(Expression<Func<TEntity, bool>> predicate);
        IQueryable<TEntity> GetPage(int index, int size, ref int count);
        IQueryable<TEntity> GetPage(int index, int size, Expression<Func<TEntity, bool>> predicate, ref int count);
        void Update(TEntity obj);
        void Remove(string id);
        void Remove(Expression<Func<TEntity, bool>> predicate);
        int SaveChanges();
    }
}
