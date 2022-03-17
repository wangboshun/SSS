from sqlalchemy import table, column
from sqlalchemy.sql import select

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
    def get_stream_data(where_str: str):
        db = db_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
        with db.get_engine().connect() as connect:
            result = connect.execution_options(stream_results=True).execute(f"select * from Test1 {where_str}")
            a = 1
            for row in result:
                a = a + 1
                d = dict(row)
                d["index"] = a
                print(f'发送数据：{d}')
                consumer_1.push(d)

    @staticmethod
    def get_data_v2():

        # metadata_obj = MetaData()
        # t1 = Table('Test1', metadata_obj,
        #            Column('Id', String, primary_key=True),
        #            Column('Name', String),
        #            Column('TM', DateTime))

        t1 = table("Test1", column("Id"), column("Name"), column("TM"))

        db = db_helper(host="127.0.0.1", port=1433, user="sa", password="123456", db="wbs", db_type=db_typeEnum.MSSQL)
        with db.get_engine().connect() as connect:
            result = connect.execute(select([t1]).where(t1.c.TM > '2020-01-01 00:00:00').limit(10).offset(10).order_by(t1.c.TM.asc()))
            for row in result:
                print("Id:", row[t1.c.Id], "; Name:", row[t1.c.Name], "; TM:", row[t1.c.TM])
