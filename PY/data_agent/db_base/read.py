from db_base.base.db_helper import db_helper
from db_base.base.db_type import db_typeEnum
from db_base.consumers.convert_consumer import consumer_1


class db_read:

    @staticmethod
    def get_data():
        db = db_helper(host="192.168.1.1", port=9000, user="default", password="123456", db="default", db_type=db_typeEnum.CK)
        with db.get_engine().connect() as connect:
            result = connect.execute("select * from Test1 Limit 10")
            for row in result:
                print(row)

    @staticmethod
    def get_stream_data():
        db = db_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
        with db.get_engine().connect() as connect:
            result = connect.execution_options(stream_results=True).execute("select * from Test1 limit 10")
            for row in result:
                d = dict(row)
                consumer_1.push(d)

    @staticmethod
    def get_ddl(table: str):
        db = db_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
        db.get_engine()
        column = db.get_ddl(table)
        print(column)
