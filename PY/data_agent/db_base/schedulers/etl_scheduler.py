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
    print('scheduler_1 开始运行')
    tm = get_next_tm('scheduler_1')
    where_str = f" where TM>'{tm[0]}' and TM<'{tm[1]}' "
    where_str = where_str + f" order by TM asc "
    db_read.get_stream_data(where_str)
    update_next_tm('scheduler_1', tm[1])


def update_next_tm(job_name, next_tm):
    """
    更新下次执行时间
    :param job_name:任务名称
    :param next_tm:下次执行时间
    """
    redis = redis_helper.get_connect()
    redis.set(job_name, next_tm)


def get_next_tm(job_name):
    """
    获取下次执行时间
    :param job_name:任务名称
    """
    redis = redis_helper.get_connect()
    now_tm = '2019-01-01 00:00:00'
    prev = redis.get(job_name)
    if prev is not None:
        now_tm = redis.get(job_name)
    next_tm = (datetime.datetime.strptime(now_tm, '%Y-%m-%d %H:%M:%S') +
               datetime.timedelta(minutes=1)).strftime('%Y-%m-%d %H:%M:%S')
    redis.set(job_name, next_tm)
    return now_tm, next_tm
