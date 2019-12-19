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
using System.Data.Common;
using System.Linq;
using System.Linq.Expressions;

namespace SSS.Infrastructure.Seedwork.Repository
{
    [DIService(ServiceLifetime.Scoped, typeof(IRepository<>))]
    public abstract class Repository<TEntity> : IRepository<TEntity>
        where TEntity : Entity, new()
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

        /// <summary>
        /// 执行sql
        /// </summary>
        /// <param name="sql"></param>
        /// <param name="parameter"></param>
        /// <returns></returns>
        public virtual int Execute(string sql, params DbParameter[] parameter)
        {
            return Db.Database.ExecuteSqlRaw(sql);
        }

        public virtual bool Add(TEntity obj, bool save = false)
        {
            DbSet.Add(obj);
            if (save)
                return SaveChanges() > 0;
            return false;
        }

        /// <summary>
        ///     批量添加
        /// </summary>
        /// <param name="list"></param>
        /// <param name="save"></param>
        public virtual bool AddList(List<TEntity> list, bool save = false)
        {
            DbSet.AddRange(list);
            if (save)
                return SaveChanges() > 0;
            return false;
        }

        /// <summary>
        ///     批量更新
        /// </summary>
        /// <param name="list"></param>
        /// <param name="save"></param>
        public virtual bool UpdateList(List<TEntity> list, bool save = false)
        {
            DbSet.UpdateRange(list);
            if (save)
                return SaveChanges() > 0;
            return false;
        }

        /// <summary>
        ///     批量删除
        /// </summary>
        /// <param name="predicate"></param>
        /// <param name="save"></param>
        public virtual bool DeleteList(Expression<Func<TEntity, bool>> predicate, bool save = false)
        {
            var list = DbSet.Where(predicate);
            foreach (TEntity item in list)
            {
                item.IsDelete = 1;
            }
            DbSet.UpdateRange(list);
            if (save)
                return SaveChanges() > 0;
            return false;
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
        public virtual IQueryable<TEntity> GetBySql(string sql, params DbParameter[] parameter)
        {
            return DbSet.FromSqlRaw(sql, GeneratorParameter(parameter));
        }

        /// <summary>
        ///     SQL查询 Lambda查询
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <param name="predicate">Lambda表达式</param>
        /// <returns></returns>
        public virtual IQueryable<TEntity> GetBySql(string sql, Expression<Func<TEntity, bool>> predicate)
        {
            return DbSet.FromSqlRaw(sql).Where(predicate);
        }

        /// <summary>
        ///     SQL查询 分页
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <param name="pageindex">页码</param>
        /// <param name="pagesize">大小</param>
        /// <param name="count">总数量</param>
        /// <returns></returns>
        public IQueryable<TEntity> GetBySql(string sql, int pageindex, int pagesize, ref int count)
        {
            var data = GetBySql(sql);
            count = data.Count();
            return data.OrderByDescending(x => x.CreateTime).Skip(pagesize * pageindex).Take(pagesize);
        }

        /// <summary>
        ///     SQL查询 Lambda查询 分页
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <param name="predicate">Lambda表达式</param>
        /// <param name="pageindex">页码</param>
        /// <param name="pagesize">大小</param>
        /// <param name="count">总数量</param>
        /// <returns></returns>
        public IQueryable<TEntity> GetBySql(string sql, Expression<Func<TEntity, bool>> predicate, int pageindex, int pagesize,
            ref int count)
        {
            var data = GetBySql(sql, predicate);
            count = data.Count();
            return data.OrderByDescending(x => x.CreateTime).Skip(pagesize * pageindex).Take(pagesize);
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
        /// <param name="pageindex">页码</param>
        /// <param name="pagesize">大小</param>
        /// <param name="count">总数量</param>
        /// <returns></returns>
        public IQueryable<TEntity> GetPage(int pageindex, int pagesize, ref int count)
        {
            count = DbSet.Count();
            return DbSet.OrderByDescending(x => x.CreateTime).Skip(pagesize * pageindex).Take(pagesize);
        }

        /// <summary>
        ///     分页查询 Lambda查询
        /// </summary>
        /// <param name="pageindex">页码</param>
        /// <param name="pagesize">大小</param>
        /// <param name="predicate">Lambda表达式</param>
        /// <param name="count">总数量</param>
        /// <returns></returns>
        public IQueryable<TEntity> GetPage(int pageindex, int pagesize, Expression<Func<TEntity, bool>> predicate, ref int count)
        {
            count = DbSet.Where(predicate).Count();
            return DbSet.OrderByDescending(x => x.CreateTime).Where(predicate).Skip(pagesize * pageindex).Take(pagesize);
        }

        /// <summary>
        /// sql分页查询，用于联表查询
        /// </summary>
        /// <typeparam name="TEntity">返回类型</typeparam>
        /// <param name="sql">sql</param>
        /// <param name="field">返回的字段</param>
        /// <param name="pageindex">页码</param>
        /// <param name="pagesize">大小</param>
        /// <returns></returns>
        public Pages<IEnumerable<TEntity>> GetPage(string sql, string field, int pageindex, int pagesize)
        {
            int count = Db.Database.Count(string.Format(sql, $" count( DISTINCT {field}.Id ) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<TEntity>(string.Format(sql + limit, $" DISTINCT {field}.* ", pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<TEntity>>(data, count);
            }
            {
                var data = Db.Database.SqlQuery<TEntity>(string.Format(sql, $" DISTINCT {field}.* "));
                return new Pages<IEnumerable<TEntity>>(data, count);
            }
        }

        /// <summary>
        ///     更新
        /// </summary>
        /// <param name="obj">实体</param>
        /// <param name="save">是否保存  默认否</param>
        public virtual bool Update(TEntity obj, bool save = false)
        {
            DbSet.Attach(obj);
            var entry = Db.Entry(obj);
            entry.State = EntityState.Modified;
            entry.Property(x => x.CreateTime).IsModified = false;
            entry.Property(x => x.Id).IsModified = false;
            if (save)
                return SaveChanges() > 0;
            return false;
        }

        /// <summary>
        ///     删除
        /// </summary>
        /// <param name="id">Id</param>
        /// <param name="save">是否保存  默认否</param>
        public virtual bool Remove(string id, bool save = false)
        {
            var model = Get(x => x.Id.Equals(id) && x.IsDelete == 0);
            if (model == null)
            {
                //_error.Execute("数据不存在或已删除,删除失败！");
                return false;
            }

            model.IsDelete = 1;
            Update(model);
            if (save)
                return SaveChanges() > 0;
            return false;
        }

        /// <summary>
        ///     删除 Lambda删除
        /// </summary>
        /// <param name="predicate">Lambda表达式</param>
        /// <param name="save">是否保存  默认否</param>
        public virtual bool Remove(Expression<Func<TEntity, bool>> predicate, bool save = false)
        {
            var model = Get(predicate);
            if (model == null)
            {
                //_error.Execute("数据不存在或已删除,删除失败！");
                return false;
            }

            model.IsDelete = 1;
            Update(model);
            if (save)
                return SaveChanges() > 0;
            return false;
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
        private object[] GeneratorParameter(params DbParameter[] parameter)
        {
            List<object> sqlparameter = new List<object>();

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
        where TEntity : Entity, new()
        where TInput : InputDtoBase
        where TOutput : OutputDtoBase
    {
        public Repository(DbcontextBase context) : base(context)
        {
        }
    }
}