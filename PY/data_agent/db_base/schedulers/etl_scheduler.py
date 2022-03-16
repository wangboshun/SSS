import datetime

from funboost import boost, BrokerEnum, fsdf_background_scheduler

from db_base.read import db_read
from db_base.utils.redis_helper import redis_helper


def init():
    fsdf_background_scheduler.add_timing_publish_job(scheduler_1, 'cron', day_of_week='*', hour='*', minute='*', second='*/5')
    # 启动定时
    fsdf_background_scheduler.start()
    # 启动消费
    scheduler_1.consume()


@boost('scheduler_1', broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM, create_logger_file=False)
def scheduler_1():
    redis = redis_helper.get_connect('192.168.1.1', 6379, 10, '123456')
    now_tm = '2019-01-01 00:00:00'
    prev = redis.get('scheduler_1')
    if prev is not None:
        now_tm = redis.get('scheduler_1')
    next_tm = (datetime.datetime.strptime(now_tm, '%Y-%m-%d %H:%M:%S') +
               datetime.timedelta(minutes=1)).strftime('%Y-%m-%d %H:%M:%S')
    print('scheduler_1 开始运行')
    where_str = f" where TM>'{now_tm}' and TM<'{next_tm}' "
    where_str = where_str + f" order by TM asc "
    db_read.get_stream_data(where_str)
    redis.set('scheduler_1', next_tm)
