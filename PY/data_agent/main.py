from db_base.read import db_read
# from db_base.schedulers.etl_scheduler import run_scheduler

if __name__ == '__main__':
    # run_scheduler()
    db_read.get_data_v2()