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
    json = json_helper.get_val("QUEUE")
    for item in json:
        job_name = item['NAME']
        pub = item['PUB']
        status = item['STATUS']
        timeout = item['TIMEOUT']
        concurrent_mode = item['MODE']
        if status != 'ON':
            continue

        kwargs = {'create_logger_file': False,'concurrent_mode': concurrent_mode, 'function_timeout': timeout, 'consuming_function': __get_func__(pub)}
        consumer = get_consumer(pub, broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM, **kwargs)

        if consumer is not None:
            cron = item['CRON']
            array = cron.split(' ')
            fsdf_background_scheduler.add_timing_publish_job(id=job_name, func=consumer, trigger='cron',
                                                             second=array[0], minute=array[1], hour=array[2],
                                                             day=array[3], month=array[4], year=array[5],
                                                             kwargs={"job_name": job_name})
            consumer.start_consuming_message()
    fsdf_background_scheduler.start()


def __get_func__(name):
    func_dict = {
        'scheduler_1': scheduler_1,
        'scheduler_2': scheduler_2,
        'scheduler_3': scheduler_3
    }
    return func_dict.get(name)


def scheduler_1(job_name):
    print(f'{job_name} 开始运行')
    tm = get_next_tm(job_name)
    db_read.get_data_v1('Test1', '*', {'TM>': tm[0], 'TM<': tm[1]})
    update_next_tm(job_name, tm[1])


def scheduler_2(job_name):
    print(f'{job_name} 开始运行')
    tm = get_next_tm(job_name)
    db_read.get_data_v2('Test1', '*', {'TM>': tm[0], 'TM<': tm[1]})
    update_next_tm(job_name, tm[1])


def scheduler_3(job_name):
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
