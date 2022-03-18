from sqlalchemy import table, column, and_, text, desc, asc
from sqlalchemy.sql import select

from db_base.base.db_helper import db_helper
from db_base.base.db_type import db_typeEnum
from db_base.consumers.convert_consumer import consumer_1, consumer_2


class db_read:

    @staticmethod
    def get_data(where_str: str):
        db = db_helper(host="192.168.1.1", port=9000, user="default", password="123456", db="default", db_type=db_typeEnum.CK)
        with db.get_engine().connect() as connect:
            result = connect.execute(f"select * from Test1 {where_str} ")
            for row in result:
                consumer_2.push(row)

    @staticmethod
    def get_stream_data(where_str: str):
        db = db_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
        with db.get_engine().connect() as connect:
            result = connect.execution_options(stream_results=True).execute(f"select * from Test1 {where_str}")
            for row in result:
                d = dict(row)
                print(f'~~~~~~发送数据：{d}~~~~~~')
                consumer_1.push(d)

    @staticmethod
    def get_data_v2(start: str, end: str, order_filed: str, order_type: str):
        t1 = table("Test1", column("Id"), column("Name"), column("TM"))
        db = db_helper(host="127.0.0.1", port=1433, user="sa", password="123456", db="wbs", db_type=db_typeEnum.MSSQL)
        with db.get_engine().connect() as connect:
            sql = select([t1]).where(and_(text(f"TM>'{start}'"), text(f"TM<'{end}'")))
            if order_type == "desc":
                sql = sql.order_by(desc(order_filed))
            elif order_type == "asc":
                sql = sql.order_by(asc(order_filed))
            result = connect.execute(sql)
            for row in result:
                d = dict(row)
                print(f'~~~~~~发送数据：{d}~~~~~~')
                consumer_2.push(d)
