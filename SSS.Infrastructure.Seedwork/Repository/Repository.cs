using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Newtonsoft.Json.Linq;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Linq.Expressions;

namespace SSS.Infrastructure.Seedwork.Repository
{
    [DIService(ServiceLifetime.Scoped, typeof(IRepository<>))]
    public abstract class Repository<TEntity> : IRepository<TEntity>
        where TEntity : Entity
    {
        protected readonly DbcontextBase Db;
        protected readonly DbSet<TEntity> DbSet;

        public Repository(DbcontextBase context)
        {
            Db = context;
            DbSet = Db.Set<TEntity>();
        }

        public virtual void Add(TEntity obj)
        {
            DbSet.Add(obj);
        }

        public virtual TEntity Get(string id)
        {
            return DbSet.Find(id);
        }

        public virtual TEntity Get(Expression<Func<TEntity, bool>> predicate)
        {
            return DbSet.FirstOrDefault(predicate);
        }

        public virtual IQueryable<TEntity> GetBySql(string sql)
        {
            return DbSet.FromSql(sql);
        }

        public virtual IQueryable<TEntity> GetBySql(string sql, params object[] parameter)
        {
            return DbSet.FromSql<TEntity>(sql, GeneratorParameter(parameter));
        }

        /// <summary>
        /// 生成参数
        /// </summary>
        /// <param name="parameter"></param>
        /// <returns></returns>
        protected SqlParameter[] GeneratorParameter(params object[] parameter)
        {
            List<SqlParameter> sqlparameter = new List<SqlParameter>();
            foreach (var item in parameter)
            {
                JObject json = JObject.FromObject(item);
                foreach (var data in json)
                {
                    string type = data.Value.Type.ToString();
                    switch (type)
                    {
                        case "String":
                            sqlparameter.Add(new SqlParameter(data.Key, data.Value.ToString()));
                            break;
                        case "Integer":
                            sqlparameter.Add(new SqlParameter(data.Key, Convert.ToInt32(data.Value)));
                            break;
                        case "Date":
                            sqlparameter.Add(new SqlParameter(data.Key, Convert.ToDateTime(data.Value)));
                            break;
                    }
                }
            }
            return sqlparameter.ToArray();
        }

        public virtual IQueryable<TEntity> GetAll()
        {
            return DbSet.OrderByDescending(x => x.CreateTime);
        }

        public virtual IQueryable<TEntity> GetAll(Expression<Func<TEntity, bool>> predicate)
        {
            return DbSet.Where(predicate).OrderByDescending(x => x.CreateTime);
        }

        public IQueryable<TEntity> GetPage(int index, int size, ref int count)
        {
            count = DbSet.Count();
            return DbSet.OrderByDescending(x => x.CreateTime).Skip(size * (index > 0 ? index - 1 : 0)).Take(size);
        }

        public IQueryable<TEntity> GetPage(int index, int size, Expression<Func<TEntity, bool>> predicate, ref int count)
        {
            count = DbSet.Where(predicate).Count();
            return DbSet.OrderByDescending(x => x.CreateTime).Where(predicate).Skip(size * (index > 0 ? index - 1 : 0)).Take(size);
        }

        public virtual void Update(TEntity obj)
        {
            DbSet.Attach(obj);
            var entry = Db.Entry(obj);
            entry.State = EntityState.Modified;
            entry.Property(x => x.CreateTime).IsModified = false;
            entry.Property(x => x.Id).IsModified = false;
        }

        public virtual void Remove(string id)
        {
            DbSet.Remove(Get(id));
        }

        public virtual void Remove(Expression<Func<TEntity, bool>> predicate)
        {
            DbSet.Remove(Get(predicate));
        }

        public int SaveChanges()
        {
            return Db.SaveChanges();
        }

        public void Dispose()
        {
            Db.Dispose();
            GC.SuppressFinalize(this);
        }
    }

    [DIService(ServiceLifetime.Scoped, typeof(IRepository<,,>))]
    public abstract class Repository<TEntity, TInput, TOutput> : Repository<TEntity>, IRepository<TEntity, TInput, TOutput>
       where TEntity : Entity
       where TInput : InputDtoBase
       where TOutput : OutputDtoBase
    {
        public Repository(DbcontextBase context) : base(context)
        {

        }

    }
}
