import pymysql
import pymysql.cursors

from db_base.base.db_type import db_typeEnum


class db_helper:
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
        self.connect = None
        self.connect_dict = {'a': 'a'}

    def get_connect(self):
        """
        获取数据库链接
        :return: 数据库链接
        """

        if self.db_type == db_typeEnum.MySQL:
            self.connect = pymysql.connect(host=self.host,
                                           port=self.port,
                                           user=self.user,
                                           password=self.password,
                                           database=self.db)
        else:
            raise Exception('不支持的数据库类型')

        connect_str = self.connect.host_info + ':' + self.db
        if connect_str in self.connect_dict:
            return self.connect_dict[connect_str]
        self.connect_dict[connect_str] = self.connect
        return self.connect

    def get_count(self, table: str, field="*", where=''):
        if self.connect is None:
            self.get_connect()

        where_str = 'WHERE'
        if where != '':
            where_str += ' ' + where

        cu = self.connect.cursor()
        cu.execute(f'select count({field}) as cnt  from {table} {where_str} ')
        cnt = cu.fetchone()
        return cnt[0]

    def get_list_data(self, table: str, field="*", where='', order_by=''):
        if self.connect is None:
            self.get_connect()

        where_str = ' WHERE'
        if where != '':
            where_str += ' ' + where
        else:
            where_str = None

        cu = self.connect.cursor()
        cu.execute(f'select {field} from {table} {where_str} {order_by}')
        return cu.fetchall()

    def get_stream_data(self, table: str, field="*", where='', order_by=''):
        if self.connect is None:
            self.get_connect()

        where_str = ' WHERE'
        if where != '':
            where_str += ' ' + where
        else:
            where_str = None

        cu = self.connect.cursor(pymysql.cursors.SSDictCursor)
        cu.execute(f'select {field} from {table} {where_str} {order_by}')
        while True:
            row = cu.fetchone()
            if not row:
                break
            print(row)

    def insert_data(self, table: str, data: dict):
        if self.connect is None:
            self.get_connect()
        sql = f"INSERT INTO `{table}` "
        field = '('
        value = '('
        for item in data:
            field += item + ','
            value += '%s,'
        field = field[:-1] + ')'
        value = value[:-1] + ')'
        sql += field + ' VALUES ' + value
        cu = self.connect.cursor()
        cu.execute(sql, tuple(data.values()))
        self.connect.commit()


# h = db_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
# h.insert_data('Test1', {'Id': 112233, 'TM': '2022-12-12 12:12:12', 'Name': '张三'})
# h.get_stream_data('Test1', where='Id=123')
# for x in range(1, 100):
#     c = h.get_count('Test1', 'Id', 'Id=123')
#     print(c)
