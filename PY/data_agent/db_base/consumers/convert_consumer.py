import time

from funboost import boost, BrokerEnum


def init():
    consumer_1.consume()


@boost('consumer_1', qps=1000, broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM, create_logger_file=False)
def consumer_1(data):
    print(f'------开始消费数据:{data}------')
    time.sleep(5)
    # raise ExceptionForRequeue(data) 重新放入队列
    # logger = get_logger(name='consumer_1', is_add_stream_handler=True, log_filename='consumer_1.log')
    # logger.info('consumer_1: %s ' % data)
    print(f'######消费数据完成:{data}######')
