from db_base.consumers import consumers_manager
from db_base.schedulers import schedulers_manager

from utils.log_helper import log_helper

if __name__ == '__main__':
    # consumers_manager.run()
    schedulers_manager.run()

    log_helper.debug('绿色')
    log_helper.info('蓝色')
    log_helper.warn('黄色')
    log_helper.error('紫红色')

    print('hello world!')  
