# -*- coding: utf-8 -*-

from pathlib import Path

from funboost.constant import BrokerEnum, ConcurrentModeEnum
from funboost.helpers import FunctionResultStatusPersistanceConfig
from funboost.utils.simple_data_class import DataClassBase

# $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  以下是中间件连接配置    $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$


MONGO_CONNECT_URL = f'mongodb://192.168.6.133:27017'  # 如果有密码连接 'mongodb://myUserAdmin:8mwTdy1klnSYepNo@192.168.199.202:27016/admin'

RABBITMQ_USER = 'slw'
RABBITMQ_PASS = 'slw123456.'
RABBITMQ_HOST = '127.0.0.1'
RABBITMQ_PORT = 5672
RABBITMQ_VIRTUAL_HOST = '/'  # my_host # 这个是rabbitmq的虚拟子host用户自己创建的，如果你想直接用rabbitmq的根host而不是使用虚拟子host，这里写 / 即可。

REDIS_HOST = '127.0.0.1'
REDIS_PASSWORD = 'slw123456.'
REDIS_PORT = 6379
REDIS_DB = 7  # redis消息队列所在db，请不要在这个db放太多其他键值对
REDIS_DB_FILTER_AND_RPC_RESULT = 8  # 如果函数做任务参数过滤 或者使用rpc获取结果，使用这个db，因为这个db的键值对多，和redis消息队列db分开

# $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ 以上是中间件连接配置    $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

# nb_log包的第几个日志模板，内置了7个模板，可以在你当前项目根目录下的nb_log_config.py文件扩展模板。
NB_LOG_FORMATER_INDEX_FOR_CONSUMER_AND_PUBLISHER = 11  # 7是简短的不可跳转，5是可点击跳转的，11是可显示ip 进程 线程的模板。
FSDF_DEVELOP_LOG_LEVEL = 50  # 作者开发时候的调试代码的日志，仅供我自己用，所以日志级别跳到最高，用户不需要管。

TIMEZONE = 'Asia/Shanghai'


class BoostDecoratorDefaultParams(DataClassBase):
    concurrent_mode = ConcurrentModeEnum.THREADING
    concurrent_num = 50
    specify_concurrent_pool = None
    specify_async_loop = None
    qps: float = 0
    is_using_distributed_frequency_control = False
    is_send_consumer_hearbeat_to_redis = False

    max_retry_times = 3

    consumin_function_decorator = None
    function_timeout = 0

    log_level = 11
    logger_prefix = ''
    create_logger_file = False
    is_show_message_get_from_broker = True
    is_print_detail_exception = True

    msg_expire_senconds = 0

    do_task_filtering = False
    task_filtering_expire_seconds = 0

    function_result_status_persistance_conf = FunctionResultStatusPersistanceConfig(False, False, 7 * 24 * 3600)

    is_using_rpc_mode = False

    is_do_not_run_by_specify_time_effect = False
    do_not_run_by_specify_time = ('10:00:00', '22:00:00')

    schedule_tasks_on_main_thread = False

    broker_kind: int = BrokerEnum.PERSISTQUEUE  # 中间件选型见3.1章节 https://funboost.readthedocs.io/zh/latest/articles/c3.html

# *********************************************** 以上是 boost装饰器的默认全局配置 *******************************************
