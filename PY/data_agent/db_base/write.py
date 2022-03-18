from sqlalchemy import table, column

from db_base.base.db_helper import db_helper
from db_base.base.db_type import db_typeEnum


class db_write:

    @staticmethod
    def insert_data():
        db = db_helper(host="192.168.1.1", port=9000, user="default", password="123456", db="default", db_type=db_typeEnum.CK)
        with db.get_engine().connect() as connect:
            result = connect.execute("INSERT INTO Test1 (Id, TM, Name) VALUES('a12', '2008-05-19 16:55:43', 'wbs');")
            print(result)

    @staticmethod
    def insert_data_v1(data):
        t2 = table("Test2", column("Id"), column("Name"), column("TM"))
        db = db_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
        with db.get_engine().connect() as connect:
            result = connect.execute(t2.insert(), data)
            print(f'完成{result.rowcount}条插入')

    @staticmethod
    def insert_data_v2(data):
        t2 = table("Test2", column("Id"), column("Name"), column("TM"))
        db = db_helper(host="127.0.0.1", port=1433, user="sa", password="123456", db="wbs", db_type=db_typeEnum.MSSQL)
        with db.get_engine().connect() as connect:
            result = connect.execute(t2.insert(), data)
            print(f'完成{result.rowcount}条插入')
