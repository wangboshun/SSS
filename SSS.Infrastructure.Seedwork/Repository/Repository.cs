using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Newtonsoft.Json.Linq;

using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Http;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;

namespace SSS.Infrastructure.Seedwork.Repository
{
    [DIService(ServiceLifetime.Scoped, typeof(IRepository<>))]
    public abstract class Repository<TEntity> : IRepository<TEntity>
        where TEntity : Entity
    {
        private readonly IErrorHandler _error;
        private readonly ILogger _logger;
        public readonly DbcontextBase Db;
        public readonly DbSet<TEntity> DbSet;

        public Repository(DbcontextBase context)
        {
            Db = context;
            DbSet = Db.Set<TEntity>();
            _error = (IErrorHandler)HttpContextService.Current.RequestServices.GetService(typeof(IErrorHandler));
            _logger = (ILogger)HttpContextService.Current.RequestServices.GetService(typeof(ILogger<Repository<TEntity>>));
        }

        public virtual void Add(TEntity obj, bool save = false)
        {
            DbSet.Add(obj);
            if (save)
                Db.SaveChanges();
        }

        /// <summary>
        ///     批量添加
        /// </summary>
        /// <param name="list"></param>
        /// <param name="save"></param>
        public virtual void AddList(List<TEntity> list, bool save = false)
        {
            DbSet.AddRange(list);
            if (save)
                Db.SaveChanges();
        }

        /// <summary>
        ///     Id查询
        /// </summary>
        /// <param name="id">Id</param>
        /// <returns></returns>
        public virtual TEntity Get(string id)
        {
            return DbSet.Find(id);
        }

        /// <summary>
        ///     Lambda查询
        /// </summary>
        /// <param name="predicate">Lambda表达式</param>
        /// <returns></returns>
        public virtual TEntity Get(Expression<Func<TEntity, bool>> predicate)
        {
            return DbSet.FirstOrDefault(predicate);
        }

        /// <summary>
        ///     SQL查询
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <returns></returns>
        public virtual IQueryable<TEntity> GetBySql(string sql)
        {
            return DbSet.FromSqlRaw(sql);
        }

        /// <summary>
        ///     SQL查询  参数化
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <param name="parameter">参数</param>
        /// <returns></returns>
        public virtual IQueryable<TEntity> GetBySql(string sql, params object[] parameter)
        {
            return DbSet.FromSqlRaw(sql, GeneratorParameter(parameter));
        }

        /// <summary>
        ///     SQL查询 Lambda查询
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <param name="predicate">Lambda表达式</param>
        /// <returns></returns>
        public IQueryable<TEntity> GetBySql(string sql, Expression<Func<TEntity, bool>> predicate)
        {
            return DbSet.FromSqlRaw(sql).Where(predicate);
        }

        /// <summary>
        ///     SQL查询 分页
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <param name="index">页码</param>
        /// <param name="size">大小</param>
        /// <param name="count">总数量</param>
        /// <returns></returns>
        public IQueryable<TEntity> GetBySql(string sql, int index, int size, ref int count)
        {
            var data = GetBySql(sql);
            count = data.Count();
            return data.OrderByDescending(x => x.CreateTime).Skip(size * index).Take(size);
        }

        /// <summary>
        ///     SQL查询 Lambda查询 分页
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <param name="predicate">Lambda表达式</param>
        /// <param name="index">页码</param>
        /// <param name="size">大小</param>
        /// <param name="count">总数量</param>
        /// <returns></returns>
        public IQueryable<TEntity> GetBySql(string sql, Expression<Func<TEntity, bool>> predicate, int index, int size,
            ref int count)
        {
            var data = GetBySql(sql, predicate);
            count = data.Count();
            return data.OrderByDescending(x => x.CreateTime).Skip(size * index).Take(size);
        }

        /// <summary>
        ///     查询所有
        /// </summary>
        /// <returns></returns>
        public virtual IQueryable<TEntity> GetAll()
        {
            return DbSet.OrderByDescending(x => x.CreateTime);
        }

        /// <summary>
        ///     查询所有 Lambda查询
        /// </summary>
        /// <param name="predicate"></param>
        /// <returns></returns>
        public virtual IQueryable<TEntity> GetAll(Expression<Func<TEntity, bool>> predicate)
        {
            return DbSet.Where(predicate).OrderByDescending(x => x.CreateTime);
        }

        /// <summary>
        ///     分页查询
        /// </summary>
        /// <param name="index">页码</param>
        /// <param name="size">大小</param>
        /// <param name="count">总数量</param>
        /// <returns></returns>
        public IQueryable<TEntity> GetPage(int index, int size, ref int count)
        {
            count = DbSet.Count();
            return DbSet.OrderByDescending(x => x.CreateTime).Skip(size * index).Take(size);
        }

        /// <summary>
        ///     分页查询 Lambda查询
        /// </summary>
        /// <param name="index">页码</param>
        /// <param name="size">大小</param>
        /// <param name="predicate">Lambda表达式</param>
        /// <param name="count">总数量</param>
        /// <returns></returns>
        public IQueryable<TEntity> GetPage(int index, int size, Expression<Func<TEntity, bool>> predicate,
            ref int count)
        {
            count = DbSet.Where(predicate).Count();
            return DbSet.OrderByDescending(x => x.CreateTime).Where(predicate).Skip(size * index).Take(size);
        }

        /// <summary>
        ///     更新
        /// </summary>
        /// <param name="obj">实体</param>
        /// <param name="save">是否保存  默认否</param>
        public virtual void Update(TEntity obj, bool save = false)
        {
            DbSet.Attach(obj);
            var entry = Db.Entry(obj);
            entry.State = EntityState.Modified;
            entry.Property(x => x.CreateTime).IsModified = false;
            entry.Property(x => x.Id).IsModified = false;
            if (save)
                Db.SaveChanges();
        }

        /// <summary>
        ///     删除
        /// </summary>
        /// <param name="id">Id</param>
        /// <param name="save">是否保存  默认否</param>
        public virtual void Remove(string id, bool save = false)
        {
            DbSet.Remove(Get(id));
            if (save)
                Db.SaveChanges();
        }

        /// <summary>
        ///     删除 Lambda删除
        /// </summary>
        /// <param name="predicate">Lambda表达式</param>
        /// <param name="save">是否保存  默认否</param>
        public virtual void Remove(Expression<Func<TEntity, bool>> predicate, bool save = false)
        {
            DbSet.Remove(Get(predicate));
            if (save)
                Db.SaveChanges();
        }

        /// <summary>
        ///     提交
        /// </summary>
        /// <returns></returns>
        public int SaveChanges()
        {
            try
            {
                return Db.SaveChanges();
            }
            catch (Exception ex)
            {
                _error.Execute(ex);
                _logger.LogError(new EventId(ex.HResult), ex, "Repository Exception");
                return 0;
            }
        }

        /// <summary>
        ///     释放
        /// </summary>
        public void Dispose()
        {
            Db.Dispose();
            GC.SuppressFinalize(this);
        }

        /// <summary>
        ///     生成参数
        /// </summary>
        /// <param name="parameter">参数</param>
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
    }

    [DIService(ServiceLifetime.Scoped, typeof(IRepository<,,>))]
    public abstract class Repository<TEntity, TInput, TOutput> : Repository<TEntity>,
        IRepository<TEntity, TInput, TOutput>
        where TEntity : Entity
        where TInput : InputDtoBase
        where TOutput : OutputDtoBase
    {
        public Repository(DbcontextBase context) : base(context)
        {
        }
    }
}