from funboost import BrokerEnum, get_consumer

from db_base.write import db_write
from utils.json_helper import json_helper

consumer_dict = {}


def publish_data(name, **kwargs):
    if name in consumer_dict:
        consumer_dict.get(name).publisher_of_same_queue.publish(kwargs)


def init():
    json = json_helper.get_val("QUEUE")
    for item in json:
        sub = item['SUB']
        status = item['STATUS']
        timeout = item['TIMEOUT']
        concurrent_mode = item['MODE']
        qps = item['QPS']
        if status != 'ON':
            continue

        kwargs = {'qps': qps, 'concurrent_mode': concurrent_mode, 'function_timeout': timeout, 'consuming_function': __get_func__(sub)}
        consumer = get_consumer(sub, broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM, **kwargs)

        if consumer is not None:
            consumer_dict[sub] = consumer
            consumer.start_consuming_message()


def __get_func__(name):
    func_dict = {
        'consumer_1': consumer_1,
        'consumer_2': consumer_2,
        'consumer_3': consumer_3
    }
    return func_dict.get(name)


def consumer_1(data, cnt):
    print(f'------consumer_1开始消费数据:{data}------')
    db_write.insert_data_v1(data)
    print(f'######consumer_1消费数据完成:{data}######')


def consumer_2(data, cnt):
    print(f'------consumer_2开始消费数据:{data}------')
    db_write.insert_data_v2(data)
    print(f'######consumer_2消费数据完成:{data}######')


def consumer_3(data, cnt):
    print(f'------consumer_3开始消费数据:{data}------')
    db_write.insert_data_v3(data)
    print(f'######consumer_3消费数据完成:{data}######')
