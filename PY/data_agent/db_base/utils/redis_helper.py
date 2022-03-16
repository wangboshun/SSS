import redis


class redis_helper:

    @staticmethod
    def get_connect(host, port, db, password):
        return redis.StrictRedis(host=host, port=port, db=db, password=password, decode_responses=True)
