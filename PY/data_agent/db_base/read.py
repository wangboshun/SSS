from db_base.base.db_helper import db_helper
from db_base.base.db_type import db_typeEnum
from db_base.consumers.convert_consumer import consumer_1


class db_read:

    @staticmethod
    def get_data_v1(table: str, field: str, where: dict):
        db = db_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
        result = db.get_list_data(table, field, where)
        cnt = db.get_count(table, field, where)
        for row in result:
            print(f'~~~~~~v1 发送数据：{row}~~~~~~')
            consumer_1.push(row, cnt)

    @staticmethod
    def get_data_v2(table: str, field: str, where: dict):
        db = db_helper(host="127.0.0.1", port=1433, user="sa", password="123456", db="wbs", db_type=db_typeEnum.MSSQL)
        result = db.get_list_data(table, field, where)
        cnt = db.get_count(table, field, where)
        for row in result:
            print(f'~~~~~~v2 发送数据：{row}~~~~~~')
            consumer_1.push(row, cnt)
