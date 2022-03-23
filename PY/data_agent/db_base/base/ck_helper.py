from clickhouse_driver import dbapi as ck_dbapi
from clickhouse_driver.dbapi.extras import DictCursor
from dbutils.pooled_db import PooledDB

from db_base.base.db_type import db_typeEnum


class ck_helper:
    def __init__(self, host: str, port: int, user: str, password: str, db: str, db_type: db_typeEnum):
        """
        构造链接
        :param host: 主机
        :param port: 端口
        :param user: 用户名
        :param password: 密码
        :param db: 数据库名
        :param db_type: 数据库类型
        """
        self.host = host
        self.port = port
        self.db = db
        self.user = user
        self.password = password
        self.db_type = db_type

    def get_connect(self):
        """
        获取数据库链接
        :return: 数据库链接
        """

        pool = PooledDB(creator=ck_dbapi, maxconnections=100, ping=2,
                        host=self.host, port=self.port, user=self.user, password=self.password, database=self.db)
        connect = pool.connection()
        return connect

    @staticmethod
    def __where__(where: dict):
        where_str = ' WHERE '
        if where is None:
            return ''

        for item in where:
            where_str += f' {item}%({item})s AND'
        where_str = where_str[:-4]
        return where_str

    def get_count(self, table: str, field="*", where=None):
        connect = self.get_connect()
        cu = connect.cursor()
        cu.execute(f'select count({field}) as cnt  from {table} {self.__where__(where)} ;', where)
        cnt = cu.fetchone()
        connect.close()
        return cnt[0]

    def get_list_data(self, table: str, field="*", where=None, order_by=''):
        connect = self.get_connect()
        cursor_kwargs = {'cursor_factory': DictCursor}
        cu = connect.cursor(**cursor_kwargs)
        cu.execute(f'select {field} from {table}  {self.__where__(where)}  {order_by} ;', where)
        d = cu.fetchall()
        connect.close()
        return d

    def get_stream_data(self, table: str, field="*", where=None, order_by=''):
        connect = self.get_connect()
        cursor_kwargs = {'cursor_factory': DictCursor}
        cu = connect.cursor(**cursor_kwargs)
        cu.set_stream_results(True, 10000)
        cu.execute(f'select {field} from {table}  {self.__where__(where)}  {order_by} ;', where)
        while True:
            row = cu.fetchone()
            if not row:
                break
            print(row)
        connect.close()

    def insert_data(self, table: str, data: dict):
        insert_sql = f"INSERT INTO `{table}` "
        field = '('
        value = '('
        for item in data:
            field += item + ','
            value += f'%({item})s,'
        field = field[:-1] + ')'
        value = value[:-1] + ')'
        insert_sql += field + ' VALUES ' + value
        connect = self.get_connect()
        cu = connect.cursor()
        cu.execute(insert_sql, data)
        connect.commit()
        connect.close()
