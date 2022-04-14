import random
from multiprocessing import Process

import pandas as pd

from db_base.write import db_write


def func1(a):
    ts = pd.date_range(start='01/01/2000', end='01/01/2022', freq='5min').to_pydatetime()
    print('时序长度', len(ts))
    data = {'Id': a}
    db_write.insert_data_v4.consume()
    for b in ts:
        data['TM'] = b
        data['SJ'] = round(random.uniform(10, 20), 2)
        db_write.insert_data_v4.push('Test1', data)


def func2(a):
    ts = pd.date_range(start='01/01/2000', end='01/01/2022', freq='5min').to_pydatetime()
    print('时序长度', len(ts))
    db_write.insert_data_v5.consume()
    list_data = []
    for b in ts:
        list_data.append({'Id': a, 'TM': b, 'SJ': round(random.uniform(10, 20), 2)})
        if len(list_data) == 100:
            db_write.insert_data_v5.push('Test1', list_data)
            list_data = []


if __name__ == "__main__":
    for a in range(10000001, 10000009):
        p = Process(target=func2, args=(a,))  # 必须加,号
        p.start()
