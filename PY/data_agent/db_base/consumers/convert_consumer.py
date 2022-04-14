from funboost import BrokerEnum, get_consumer, boost

from db_base.write import db_write
from utils.check_helper import check_helper
from utils.json_helper import json_helper

consumer_dict = {}


def publish_data(name, **kwargs):
    if name in consumer_dict:
        consumer_dict.get(name).push(kwargs)


def init():
    json = json_helper.get_val("QUEUE")
    for item in json:
        sub = item['SUB']
        status = item['STATUS']
        timeout = item['TIMEOUT']
        concurrent_mode = item['MODE']
        qps = item['QPS']
        create_logger_file = item['LOG']
        if status != 'ON':
            continue

        create_logger_file = True if create_logger_file == 'ON' else False
        kwargs = {'create_logger_file': create_logger_file, 'qps': qps, 'concurrent_mode': concurrent_mode, 'function_timeout': timeout}
        __boost = boost(sub, broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM, **kwargs)(__get_func__(sub))
        consumer_dict[sub] = __boost
        __boost.multi_process_start(len([elem for elem in json if elem['STATUS'] != 'OFF']))


def __get_func__(name):
    func_dict = {
        'consumer_11': consumer_1,
        'consumer_22': consumer_2,
        'consumer_33': consumer_3
    }
    return func_dict.get(name)


def consumer_1(data, cnt):
    table = 'Test2'
    print(f'------consumer_1开始消费数据:{data}------')
    db_write.insert_data_v1(table, data)
    check_helper.isEmpty(data, '')
    print(f'######consumer_1消费数据完成:{data}######')


def consumer_2(data, cnt):
    table = 'Test2'
    print(f'------consumer_2开始消费数据:{data}------')
    db_write.insert_data_v2(table, data)
    print(f'######consumer_2消费数据完成:{data}######')


def consumer_3(data, cnt):
    table = 'Test2'
    print(f'------consumer_3开始消费数据:{data}------')
    db_write.insert_data_v3(table, data)
    print(f'######consumer_3消费数据完成:{data}######')
