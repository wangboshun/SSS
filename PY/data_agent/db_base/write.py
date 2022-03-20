from db_base.base.db_helper import db_helper
from db_base.base.db_type import db_typeEnum


class db_write:

    @staticmethod
    def insert_data_v1(data):
        db = db_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
        db.insert_data('Test2', data)

    @staticmethod
    def insert_data_v2(data):
        db = db_helper(host="127.0.0.1", port=1433, user="sa", password="123456", db="wbs", db_type=db_typeEnum.MSSQL)
        db.insert_data('Test2', data)
