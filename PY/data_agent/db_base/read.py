from db_base.base.ck_helper import ck_helper
from db_base.base.db_type import db_typeEnum
from db_base.base.mssql_helper import mssql_helper
from db_base.base.mysql_helper import mysql_helper
from db_base.consumers.convert_consumer import consumer_1, consumer_2, consumer_3


class db_read:

    @staticmethod
    def get_data_v1(table: str, field: str, where: dict):
        db = mysql_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
        result = db.get_list_data(table, field, where)
        cnt = db.get_count(table, field, where)
        for row in result:
            print(f'~~~~~~v1 发送数据：{row}~~~~~~')
            consumer_1.push(row, cnt)

    @staticmethod
    def get_data_v2(table: str, field: str, where: dict):
        db = mssql_helper(host="127.0.0.1", port=1433, user="sa", password="123456", db="wbs", db_type=db_typeEnum.MSSQL)
        result = db.get_list_data(table, field, where)
        cnt = db.get_count(table, field, where)
        for row in result:
            print(f'~~~~~~v2 发送数据：{row}~~~~~~')
            consumer_2.push(row, cnt)

    @staticmethod
    def get_data_v3(table: str, field: str, where: dict):
        db = ck_helper(host="192.168.1.1", port=9000, user="default", password="123456", db="default", db_type=db_typeEnum.CK)
        result = db.get_list_data(table, field, where)
        cnt = db.get_count(table, field, where)
        for row in result:
            print(f'~~~~~~v3 发送数据：{row}~~~~~~')
            consumer_3.push(row, cnt)
