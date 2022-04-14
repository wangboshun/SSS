from funboost import boost, BrokerEnum

from db_base.base.ck_helper import ck_helper
from db_base.base.db_type import db_typeEnum
from db_base.base.mssql_helper import mssql_helper
from db_base.base.mysql_helper import mysql_helper
from utils.json_helper import json_helper
from utils.linq.linq import Linq


class db_write:

    @staticmethod
    def insert_data_v1(table, data):
        config = Linq(json_helper.get_val("DB")).first(lambda x: x['NAME'] == 'MYSQL_1')
        db = mysql_helper(host=config['HOST'], port=config['PORT'], user=config['USER'],
                          password=config['PASSWORD'], db=config['DATABASE'], db_type=db_typeEnum.MySQL)
        db.insert_data(table, data)

    @staticmethod
    def insert_data_v2(table, data):
        config = Linq(json_helper.get_val("DB")).first(lambda x: x['NAME'] == 'MSSQL_1')
        db = mssql_helper(host=config['HOST'], port=config['PORT'], user=config['USER'],
                          password=config['PASSWORD'], db=config['DATABASE'], db_type=db_typeEnum.MSSQL)
        db.insert_data(table, data)

    @staticmethod
    def insert_data_v3(table, data):
        config = Linq(json_helper.get_val("DB")).first(lambda x: x['NAME'] == 'MCK_1')
        db = ck_helper(host=config['HOST'], port=config['PORT'], user=config['USER'],
                       password=config['PASSWORD'], db=config['DATABASE'], db_type=db_typeEnum.CK)
        db.insert_data(table, data)

    @staticmethod
    @boost('insert_data_v4', qps=100000, broker_kind=BrokerEnum.MEMORY_QUEUE)
    def insert_data_v4(table, data):
        config = Linq(json_helper.get_val("DB")).first(lambda x: x['NAME'] == 'MYSQL_1')
        db = mysql_helper(host=config['HOST'], port=config['PORT'], user=config['USER'],
                          password=config['PASSWORD'], db=config['DATABASE'], db_type=db_typeEnum.MySQL)
        db.insert_data(table, data)

    @staticmethod
    @boost('insert_data_v5', qps=100000, broker_kind=BrokerEnum.MEMORY_QUEUE)
    def insert_data_v5(table, list_data):
        config = Linq(json_helper.get_val("DB")).first(lambda x: x['NAME'] == 'MYSQL_1')
        db = mysql_helper(host=config['HOST'], port=config['PORT'], user=config['USER'],
                          password=config['PASSWORD'], db=config['DATABASE'], db_type=db_typeEnum.MySQL)
        db.insert_list_data(table, list_data)
