﻿using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;

using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Common;

namespace SSS.Infrastructure.Util.Ef
{
    public static class DbContextEx
    {
        public static int Count(this DatabaseFacade facade, string sql, params object[] parameters)
        {
            var cmd = CreateCommand(facade, sql, out var conn, parameters);
            var count = Convert.ToInt32(cmd.ExecuteScalar());
            conn.Close();
            return count;
        }

        public static DataTable SqlQuery(this DatabaseFacade facade, string sql, params object[] parameters)
        {
            var cmd = CreateCommand(facade, sql, out var conn, parameters);
            var reader = cmd.ExecuteReader();
            var dt = new DataTable();
            dt.Load(reader);
            reader.Close();
            conn.Close();
            return dt;
        }

        public static IEnumerable<T> SqlQuery<T>(this DatabaseFacade facade, string sql, params object[] parameters) where T : class, new()
        {
            var dt = SqlQuery(facade, sql, parameters);
            return dt.ToEnumerable<T>();
        }

        /// <summary>
        /// DataTable转List
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="dt"></param>
        /// <returns></returns>
        public static IEnumerable<T> ToEnumerable<T>(this DataTable dt) where T : class, new()
        {
            try
            {
                if (dt == null || dt.Rows.Count == 0)
                    return null;
                var ts = new List<T>();
                string tempName;
                foreach (DataRow dr in dt.Rows)
                {
                    var t = new T();
                    // 获得此模型的公共属性
                    var propertys = t.GetType().GetProperties();
                    foreach (var pi in propertys)
                    {
                        tempName = pi.Name;
                        if (dt.Columns.Contains(tempName))
                        {
                            if (!pi.CanWrite)
                                continue;
                            var value = dr[tempName];
                            if (value is DBNull)
                                continue;
                            switch (pi.PropertyType.Name.ToLower())
                            {
                                case "string" when value.GetType().Name.ToLower() == "guid":
                                    pi.SetValue(t, value.ToString(), null);
                                    break;

                                case "string" when value.GetType().Name.ToLower() == "datetime":
                                    pi.SetValue(t, Convert.ToDateTime(value).ToString("yyyy-MM-dd HH:mm:ss"), null);
                                    break;

                                case "string":
                                    pi.SetValue(t, Convert.ToString(value), null);
                                    break;

                                case "int32":
                                case "nullable`1":
                                    pi.SetValue(t, Convert.ToInt32(value), null);
                                    break;

                                case "decimal":
                                    pi.SetValue(t, Convert.ToDecimal(value), null);
                                    break;

                                case "datetime":
                                    pi.SetValue(t, Convert.ToDateTime(value), null);
                                    break;

                                case "boolean":
                                    pi.SetValue(t, Convert.ToBoolean(value), null);
                                    break;
                            }
                        }
                    }

                    ts.Add(t);
                }

                return ts;
            }
            catch (Exception)
            {
                return null;
            }
        }

        private static void CombineParams(ref DbCommand command, params object[] parameters)
        {
            if (parameters != null)
                foreach (DbParameter parameter in parameters)
                {
                    if (!parameter.ParameterName.Contains("@"))
                        parameter.ParameterName = $"@{parameter.ParameterName}";
                    command.Parameters.Add(parameter);
                }
        }

        private static DbCommand CreateCommand(DatabaseFacade facade, string sql, out DbConnection dbConn, params object[] parameters)
        {
            var conn = facade.GetDbConnection();
            dbConn = conn;
            conn.Open();
            var cmd = conn.CreateCommand();

            cmd.CommandText = sql;
            CombineParams(ref cmd, parameters);
            return cmd;
        }
    }
}