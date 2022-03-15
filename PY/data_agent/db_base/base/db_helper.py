from sqlalchemy import create_engine
from sqlalchemy.engine import reflection

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
        self.engine = None

    def get_engine(self):
        """
        获取数据库链接
        :return: 数据库链接
        """
        if self.db_type == db_typeEnum.MySQL:
            provider = 'mysql+pymysql'
        elif self.db_type == db_typeEnum.CK:
            provider = 'clickhouse+native'
        elif self.db_type == db_typeEnum.PGSQL:
            provider = 'postgresql'
        elif self.db_type == db_typeEnum.SQLITE:
            provider = 'sqlite'
        elif self.db_type == db_typeEnum.MSSQL:
            provider = 'mssql+pymssql'
        else:
            raise Exception('不支持的数据库类型')

        self.engine = create_engine(f'{provider}://{self.user}:{self.password}@{self.host}:{self.port}/{self.db}', echo=True, echo_pool=True)
        return self.engine

    def get_ddl(self, table: str):
        """
        获取表结构
        :param table: 表名
        :return: 表结构
        """
        reflect = reflection.Inspector.from_engine(self.engine)
        column = reflect.get_columns(table)
        return column
