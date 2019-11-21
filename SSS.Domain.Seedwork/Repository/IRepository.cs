﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Seedwork.Repository
{
    public interface IRepository<TEntity, TInput, TOutput> : IRepository<TEntity>
        where TEntity : Entity
        where TInput : InputDtoBase
        where TOutput : OutputDtoBase
    {
    }

    public interface IRepository<TEntity> : IDisposable
        where TEntity : Entity
    {
        void Add(TEntity obj, bool save = false);
        void AddList(List<TEntity> list, bool save = false);
        TEntity Get(string id);
        TEntity Get(Expression<Func<TEntity, bool>> predicate);
        IQueryable<TEntity> GetBySql(string sql);
        IQueryable<TEntity> GetBySql(string sql, params object[] parameter);
        IQueryable<TEntity> GetBySql(string sql, int index, int size,ref int count);
        IQueryable<TEntity> GetBySql(string sql, Expression<Func<TEntity, bool>> predicate, int index, int size,ref int count);
        IQueryable<TEntity> GetAll();
        IQueryable<TEntity> GetAll(Expression<Func<TEntity, bool>> predicate);
        IQueryable<TEntity> GetPage(int index, int size, ref int count);
        IQueryable<TEntity> GetPage(int index, int size, Expression<Func<TEntity, bool>> predicate, ref int count);
        void Update(TEntity obj, bool save = false);
        void Remove(string id, bool save = false);
        void Remove(Expression<Func<TEntity, bool>> predicate, bool save = false);
        int SaveChanges();
    }
}