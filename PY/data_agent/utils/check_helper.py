from utils.calc_helper import calc_helper


class check_helper:

    @staticmethod
    def isEmpty(data: dict, key: str):
        """
        检查是否为空字符串
        :param data:
        :param key:
        :return:
        """
        if key in data:
            if data[key] or data[key].ispace() or data[key].lower() == 'null':  # 如果是None、空字符串、空格、null，则返回True
                return True
        return False

    @staticmethod
    def isZero(data: dict, key: str):
        """
        检查是否为零值
        :param data:
        :param key:
        :return:
        """
        if key in data:
            v1 = data[key]
            if type(v1) == int or type(v1) == float:
                if v1 == 0:
                    return True
            elif type(v1) == str:
                if float(v1) == 0:
                    return True
        return False

    @staticmethod
    def checkMaxMin(data: dict, key: str, v2):
        """
        检测最大最小值
        :return:
        """
        if key in data:
            v1 = data[key]
            if type(v1) == str:
                if v1.find('.') >= 0:
                    v1 = float(v1)
                else:
                    v1 = int(v1)
            if v1 > v2:
                return calc_helper.isGreater(v1, v2)
            elif v1 < v2:
                return calc_helper.isLess(v1, v2)

    @staticmethod
    def checkRate(data: dict, key: str, v2, rate):
        """
        检测比率
        :return:
        """
        if key in data:
            v1 = data[key]
            if type(v1) == str:
                if v1.find('.') >= 0:
                    v1 = float(v1)
                else:
                    v1 = int(v1)

            if v1 == 0 or v2 == 0 or rate <= 0:
                return False
            if v2 < 1 and v1 < 1:  # 如果都小于1，不比较
                return False

            if v1 > v2:
                return calc_helper.isGreaterRate(v1, v2, rate)
            elif v1 < v2:
                return calc_helper.isLessRate(v1, v2, rate)

    @staticmethod
    def checkTimes(data: dict, key: str, v2, times):
        """
        检测倍数
        :return:
        """
        if key in data:
            v1 = data[key]
            if type(v1) == str:
                if v1.find('.') >= 0:
                    v1 = float(v1)
                else:
                    v1 = int(v1)

            if v1 == 0 or v2 == 0 or times <= 0:
                return False
            if v2 < 1 and v1 < 1:  # 如果都小于1，不比较
                return False

            if v1 > v2:
                return calc_helper.isGreaterTimes(v1, v2, times)
            elif v1 < v2:
                return calc_helper.isLessTimes(v1, v2, times)
