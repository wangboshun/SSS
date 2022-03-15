from funboost import boost, BrokerEnum


@boost('consumer_1', qps=1, broker_kind=BrokerEnum.LOCAL_PYTHON_QUEUE)
def consumer_1(data):
    print('收到数据：',data)


consumer_1.consume()
