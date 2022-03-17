import redis

from db_base.utils.json_helper import json_helper


class redis_helper:

    @staticmethod
    def get_connect():
        host = json_helper.get_val("REDIS:HOST")[0]
        port = json_helper.get_val("REDIS:PORT")[0]
        db = json_helper.get_val("REDIS:DB")[0]
        password = json_helper.get_val("REDIS:PASSWORD")[0]
        return redis.StrictRedis(host=host, port=port, db=db, password=password, decode_responses=True)
