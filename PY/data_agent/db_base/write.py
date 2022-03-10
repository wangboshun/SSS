from db_base.base.db_helper import db_helper
from db_base.base.db_type import db_typeEnum


def insert_data():
    db = db_helper(host="192.168.1.1", port=9000, user="default", password="123456", db="default", db_type=db_typeEnum.CK)
    with db.get_engine().connect() as connect:
        result = connect.execute("INSERT INTO Test1 (Id, TM, Name) VALUES('a12', '2008-05-19 16:55:43', 'wbs');")
        print(result)
