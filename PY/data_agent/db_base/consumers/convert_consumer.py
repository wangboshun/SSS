from funboost import boost, BrokerEnum
from nb_log import get_logger


@boost('consumer_1', qps=1000, broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM)
def consumer_1(data):
    logger = get_logger(name='consumer_1', is_add_stream_handler=True, log_filename='consumer_1.log')
    logger.info('consumer_1: %s' % data)
    print(f'consumer_1:{data["index"]} 消费完毕')


consumer_1.consume()
