from multiprocessing import Pool
import time


def worker(x):
    for j in range(1, 11):
        print("%d:%d" % (x, j))
        time.sleep(1)


pool = Pool(3)  # run 3 process simultaneously
for i in range(1, 11):
    print(i)
    pool.apply_async(worker, (i,))

if __name__ == '__main__':
    print("---start---")
    pool.close()  # 关闭进程池，不允许继续添加进程
    pool.join()  # 等待进程池中的所有进程结束
    print("---end---")
