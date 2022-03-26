from nb_log import LogManager


class log_helper:
    logger_info = LogManager('info').get_logger_and_add_handlers(log_filename='info.log')
    logger_warn = LogManager('warn').get_logger_and_add_handlers(log_filename='warn.log')
    logger_error = LogManager('error').get_logger_and_add_handlers(log_filename='error.log')
    logger_debug = LogManager('debug').get_logger_and_add_handlers(log_filename='debug.log')

    @classmethod
    def info(cls, msg):
        cls.logger_info.info(msg)

    @classmethod
    def warn(cls, msg):
        cls.logger_warn.warn(msg)

    @classmethod
    def error(cls, msg):
        cls.logger_error.error(msg)

    @classmethod
    def debug(cls, msg):
        cls.logger_debug.debug(msg)
