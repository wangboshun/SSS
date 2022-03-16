from funboost import boost, BrokerEnum, ExceptionForRequeue
from nb_log import get_logger


@boost('consumer_1', qps=1000, broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM,create_logger_file=False)
def consumer_1(data):
    # raise ExceptionForRequeue(data) 重新放入队列
    logger = get_logger(name='consumer_1', is_add_stream_handler=True, log_filename='consumer_1.log')
    logger.info('consumer_1: %s' % data)
    print(f'consumer_1:{data["index"]} 消费完毕')


# consumer_1.consume()
