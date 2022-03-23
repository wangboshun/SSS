import datetime

from funboost import BrokerEnum, fsdf_background_scheduler, get_consumer

from db_base.read import db_read
from utils.json_helper import json_helper
from utils.redis_helper import redis_helper


def init():
    """
    初始化定时任务
    :return:
    """
    json = json_helper.get_val("SCHEDULERS")
    for item in json:
        consumer = None
        name = item['NAME']
        func = item['FUNC']
        if func == 'scheduler_1':
            pass
            # consumer = get_consumer(name, consuming_function=scheduler_1, concurrent_mode=5, broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM)
        elif func == 'scheduler_2':
            pass
            # consumer = get_consumer(name, consuming_function=scheduler_2, concurrent_mode=5, broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM)
        elif func == 'scheduler_3':
            consumer = get_consumer(name, consuming_function=scheduler_3, concurrent_mode=5, broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM)

        if consumer is not None:
            consumer.start_consuming_message()
            fsdf_background_scheduler.add_timing_publish_job(id=name, func=consumer, trigger='cron', day_of_week='*',
                                                             hour='*', minute='*', second='*/30', kwargs={"job_name": name})
    fsdf_background_scheduler.start()


def scheduler_1(job_name: str):
    pass
    print(f'{job_name} 开始运行')
    tm = get_next_tm(job_name)
    db_read.get_data_v1('Test1', '*', {'TM>': tm[0], 'TM<': tm[1]})
    update_next_tm(job_name, tm[1])


def scheduler_2(job_name: str):
    print(f'{job_name} 开始运行')
    tm = get_next_tm(job_name)
    db_read.get_data_v2('Test1', '*', {'TM>': tm[0], 'TM<': tm[1]})
    update_next_tm(job_name, tm[1])


def scheduler_3(job_name: str):
    print(f'{job_name} 开始运行')
    tm = get_next_tm(job_name)
    db_read.get_data_v3('Test1', '*', {'TM>': tm[0], 'TM<': tm[1]})
    update_next_tm(job_name, tm[1])


def update_next_tm(job_name: str, next_tm: str):
    """
    更新下次执行时间
    :param job_name:任务名称
    :param next_tm:下次执行时间
    """
    redis = redis_helper.get_connect()
    redis.set(job_name, next_tm)


def get_next_tm(job_name: str):
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
               datetime.timedelta(days=1)).strftime('%Y-%m-%d %H:%M:%S')
    redis.set(job_name, next_tm)
    return now_tm, next_tm
