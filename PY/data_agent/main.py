from db_base.consumers import consumers_manager
from db_base.schedulers import schedulers_manager

if __name__ == '__main__':
    consumers_manager.run()
    schedulers_manager.run()
    print('hello world!')
