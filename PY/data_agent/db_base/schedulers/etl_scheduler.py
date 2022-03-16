from funboost import boost, BrokerEnum, fsdf_background_scheduler, timing_publish_deco

from db_base.read import db_read


def run_scheduler():
    fsdf_background_scheduler.add_job(timing_publish_deco(scheduler_1), 'interval', id='scheduler_1_job', seconds=10)

    # 启动定时
    fsdf_background_scheduler.start()
    # 启动消费
    scheduler_1.consume()


@boost('scheduler_1', broker_kind=BrokerEnum.RABBITMQ_AMQPSTORM,create_logger_file=False)
def scheduler_1():
    print('scheduler_1 开始运行')
    db_read.get_stream_data()
