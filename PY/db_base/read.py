from db_base.base.db_helper import db_helper
from db_base.base.db_type import db_typeEnum


def get_data():
    db = db_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
    with db.get_engine().connect() as connect:
        result = connect.execute("select * from Test1 Limit 10")
        for row in result:
            print(row)


def get_stream_data():
    db = db_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
    with db.get_engine().connect() as connect:
        index = 1
        result = connect.execution_options(stream_results=True).execute("select * from Test1")
        for row in result:
            index = index + 1
            d = row._asdict()
            print(index, d)


def get_ddl(table: str):
    db = db_helper(host="192.168.1.1", port=3306, user="root", password="123456", db="wbs", db_type=db_typeEnum.MySQL)
    db.get_engine()
    column = db.get_ddl(table)
    print(column)


get_ddl("Test1")
