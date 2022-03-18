import redis

from .json_helper import json_helper


class redis_helper:
    redis_client = None

    @classmethod
    def get_connect(cls):
        if redis_helper.redis_client is not None:
            return cls.redis_client
        host = json_helper.get_val("REDIS:HOST")
        port = json_helper.get_val("REDIS:PORT")
        db = json_helper.get_val("REDIS:DB")
        password = json_helper.get_val("REDIS:PASSWORD")
        cls.redis_client = redis.StrictRedis(host=host, port=port, db=db, password=password, decode_responses=True)
        return cls.redis_client
