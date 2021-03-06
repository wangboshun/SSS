﻿using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Newtonsoft.Json.Linq;

using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.DI;
using SSS.Infrastructure.Util.Ef;
using SSS.Infrastructure.Util.Lambda;

using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Linq;
using System.Linq.Expressions;

namespace SSS.Infrastructure.Seedwork.Repository
{
    [DIService(ServiceLifetime.Scoped, typeof(IRepository<>))]
    public class Repository<TEntity> : IRepository<TEntity>
        where TEntity : Entity, new()
    {
        public readonly DbContextBase Db;
        public readonly DbSet<TEntity> DbSet;
        private readonly IErrorHandler _error;
        private readonly ILogger _logger;

        public Repository(DbContextBase context)
        {
            Db = context;
            DbSet = Db.Set<TEntity>();
            _error = IocEx.Instance.GetService<IErrorHandler>();
            _logger = IocEx.Instance.GetService<ILogger<Repository<TEntity>>>();
        }

        #region 添加

        public virtual bool Add(TEntity obj, bool save = false)
        {
            DbSet.Add(obj);
            if (save)
                return SaveChanges() > 0;
            return false;
        }

        #endregion 添加

        #region 更新

        /// <summary>
        /// 更新
        /// </summary>
        /// <param name="obj">实体</param>
        /// <param name="save">是否保存 默认否</param>
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

        #endregion 更新

        #region Execute

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

        #endregion Execute

        #region 删除

        /// <summary>
        /// 删除
        /// </summary>
        /// <param name="id">Id</param>
        /// <param name="save">是否保存 默认否</param>
        public virtual bool Remove(string id, bool save = false)
        {
            var model = Get(id);
            if (model == null)
                //_error.Execute("数据不存在或已删除,删除失败！");
                return false;

            model.IsDelete = 1;
            Update(model);
            if (save)
                return SaveChanges() > 0;
            return false;
        }

        /// <summary>
        /// 删除 Lambda删除
        /// </summary>
        /// <param name="predicate">Lambda表达式</param>
        /// <param name="save">是否保存 默认否</param>
        public virtual bool Remove(Expression<Func<TEntity, bool>> predicate, bool save = false,
            bool have_delete = false)
        {
            if (!have_delete)
                predicate = predicate.And(x => x.IsDelete == 0);

            var model = Get(predicate);
            if (model == null)
                //_error.Execute("数据不存在或已删除,删除失败！");
                return false;

            model.IsDelete = 1;
            Update(model);
            if (save)
                return SaveChanges() > 0;
            return false;
        }

        #endregion 删除

        #region 查询

        /// <summary>
        /// Id查询
        /// </summary>
        /// <param name="id">Id</param>
        /// <returns></returns>
        public virtual TEntity Get(string id, bool have_delete = false)
        {
            if (have_delete)
                return DbSet.Find(id);

            return DbSet.FirstOrDefault(x => x.Id.Equals(id) && x.IsDelete == 0);
        }

        /// <summary>
        /// Lambda查询
        /// </summary>
        /// <param name="predicate">Lambda表达式</param>
        /// <returns></returns>
        public virtual TEntity Get(Expression<Func<TEntity, bool>> predicate, bool have_delete = false)
        {
            if (!have_delete)
                predicate = predicate.And(x => x.IsDelete == 0);

            return DbSet.FirstOrDefault(predicate);
        }

        /// <summary>
        /// 查询所有
        /// </summary>
        /// <returns></returns>
        public virtual IQueryable<TEntity> GetAll()
        {
            return DbSet.OrderByDescending(x => x.CreateTime);
        }

        /// <summary>
        /// 查询所有 Lambda查询
        /// </summary>
        /// <param name="predicate"></param>
        /// <returns></returns>
        public virtual IQueryable<TEntity> GetAll(Expression<Func<TEntity, bool>> predicate, bool have_delete = false)
        {
            if (!have_delete)
                predicate = predicate.And(x => x.IsDelete == 0);

            return DbSet.Where(predicate).OrderByDescending(x => x.CreateTime);
        }

        /// <summary>
        /// SQL查询
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <returns></returns>
        public virtual IQueryable<TEntity> GetBySql(string sql)
        {
            return DbSet.FromSqlRaw(sql);
        }

        /// <summary>
        /// SQL查询 参数化
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <param name="parameter">参数</param>
        /// <returns></returns>
        public virtual IQueryable<TEntity> GetBySql(string sql, params DbParameter[] parameter)
        {
            return DbSet.FromSqlRaw(sql, GeneratorParameter(parameter));
        }

        /// <summary>
        /// SQL查询 Lambda查询
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <param name="predicate">Lambda表达式</param>
        /// <returns></returns>
        public virtual IQueryable<TEntity> GetBySql(string sql, Expression<Func<TEntity, bool>> predicate,
            bool have_delete = false)
        {
            if (!have_delete)
                predicate = predicate.And(x => x.IsDelete == 0);

            return DbSet.FromSqlRaw(sql).Where(predicate);
        }

        /// <summary>
        /// SQL查询 分页
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
        /// SQL查询 Lambda查询 分页
        /// </summary>
        /// <param name="sql">SQL</param>
        /// <param name="predicate">Lambda表达式</param>
        /// <param name="pageindex">页码</param>
        /// <param name="pagesize">大小</param>
        /// <param name="count">总数量</param>
        /// <returns></returns>
        public IQueryable<TEntity> GetBySql(string sql, Expression<Func<TEntity, bool>> predicate, int pageindex,
            int pagesize, ref int count, bool have_delete = false)
        {
            if (!have_delete)
                predicate = predicate.And(x => x.IsDelete == 0);

            var data = GetBySql(sql, predicate);
            count = data.Count();
            return data.OrderByDescending(x => x.CreateTime).Skip(pagesize * pageindex).Take(pagesize);
        }

        /// <summary>
        /// 分页查询
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
        /// 分页查询 Lambda查询
        /// </summary>
        /// <param name="pageindex">页码</param>
        /// <param name="pagesize">大小</param>
        /// <param name="predicate">Lambda表达式</param>
        /// <param name="count">总数量</param>
        /// <returns></returns>
        public IQueryable<TEntity> GetPage(int pageindex, int pagesize, Expression<Func<TEntity, bool>> predicate,
            ref int count, bool have_delete = false)
        {
            if (!have_delete)
                predicate = predicate.And(x => x.IsDelete == 0);

            count = DbSet.Where(predicate).Count();
            return DbSet.OrderByDescending(x => x.CreateTime).Where(predicate).Skip(pagesize * pageindex)
                .Take(pagesize);
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
            var count = Db.Database.Count(string.Format(sql, $" count( DISTINCT {field}.Id ) "));

            if (pageindex > 0 && pagesize > 0)
            {
                var limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<TEntity>(string.Format(sql + limit, $" DISTINCT {field}.* ",
                    pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<TEntity>>(data, count);
            }

            {
                var data = Db.Database.SqlQuery<TEntity>(string.Format(sql, $" DISTINCT {field}.* "));
                return new Pages<IEnumerable<TEntity>>(data, count);
            }
        }

        /// <summary>
        /// 是否存在
        /// </summary>
        /// <param name="id"></param>
        /// <param name="have_delete"></param>
        /// <returns></returns>
        public virtual bool Have(string id, bool have_delete = false)
        {
            return DbSet.Any(x => x.Id.Equals(id) && x.IsDelete == 0);
        }

        /// <summary>
        /// 是否存在 Lambda表达式
        /// </summary>
        /// <param name="predicate">Lambda表达式</param>
        /// <returns></returns>
        public virtual bool Have(Expression<Func<TEntity, bool>> predicate, bool have_delete = false)
        {
            if (!have_delete)
                predicate = predicate.And(x => x.IsDelete == 0);

            return DbSet.Any(predicate);
        }

        #endregion 查询

        #region 批量操作

        /// <summary>
        /// 批量添加
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
        /// 批量删除
        /// </summary>
        /// <param name="predicate"></param>
        /// <param name="save"></param>
        public virtual bool DeleteList(Expression<Func<TEntity, bool>> predicate, bool save = false,
            bool have_delete = false)
        {
            if (!have_delete)
                predicate = predicate.And(x => x.IsDelete == 0);

            var list = DbSet.Where(predicate);
            foreach (var item in list) item.IsDelete = 1;

            DbSet.UpdateRange(list);
            if (save)
                return SaveChanges() > 0;
            return false;
        }

        /// <summary>
        /// 批量更新
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

        #endregion 批量操作

        #region 其他

        /// <summary>
        /// 释放
        /// </summary>
        public void Dispose()
        {
            Db.Dispose();
            GC.SuppressFinalize(this);
        }

        /// <summary>
        /// 提交
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
        /// 生成参数
        /// </summary>
        /// <param name="parameter">参数</param>
        /// <returns></returns>
        private object[] GeneratorParameter(params DbParameter[] parameter)
        {
            var sqlparameter = new List<object>();

            foreach (var item in parameter)
            {
                var json = JObject.FromObject(item);
                foreach (var data in json)
                {
                    var type = data.Value.Type.ToString();
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

        #endregion 其他
    }

    [DIService(ServiceLifetime.Scoped, typeof(IRepository<,,>))]
    public abstract class Repository<TEntity, TInput, TOutput> : Repository<TEntity>,
        IRepository<TEntity, TInput, TOutput>
        where TEntity : Entity, new()
        where TInput : InputDtoBase
        where TOutput : OutputDtoBase
    {
        public Repository(DbContextBase context) : base(context)
        {
        }
    }
}