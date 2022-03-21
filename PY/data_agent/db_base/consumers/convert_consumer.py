from funboost import boost, BrokerEnum

from db_base.write import db_write


def init():
    consumer_1.consume()
    consumer_2.consume()


@boost('consumer_1', qps=10000, broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM, create_logger_file=False)
def consumer_1(data, cnt):
    print(f'------consumer_1开始消费数据:{data}------')
    db_write.insert_data_v1(data)
    print(f'######consumer_1消费数据完成:{data}######')


@boost('consumer_2', qps=1, concurrent_mode=5, broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM, create_logger_file=False)
def consumer_2(data, cnt):
    print(f'------consumer_2开始消费数据:{data}------')
    # db_write.insert_data_v2(data)
    # print(f'######consumer_2消费数据完成:{data}######')
