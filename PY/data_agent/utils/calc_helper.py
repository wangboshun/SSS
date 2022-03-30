class calc_helper:

    @staticmethod
    def isGreater(v1, v2):
        """
        计算是否大于某个值
        :param v1:
        :param v2:
        :return:
        """
        if v1 > v2:
            return True
        return False

    @staticmethod
    def isLess(v1, v2):
        """
        计算是否小于某个值
        :param v1:
        :param v2:
        :return:
        """
        if v1 < v2:
            return True
        return False

    @staticmethod
    def isGreaterRate(v1, v2, rate):
        """
        计算是否大于某个百分比
        :param v1:
        :param v2:
        :param rate:百分比，如0.5，表示大于50%
        :return:
        """
        rate = 1 + rate
        if v1 > v2 * rate:
            return True
        return False

    @staticmethod
    def isLessRate(v1, v2, rate):
        """
        计算是否小于某个百分比
        :param v1:
        :param v2:
        :param rate:百分比，如0.5，表示小于50%
        :return:
        """
        rate = 1 - rate
        if v1 < v2 * rate:
            return True
        return False

    @staticmethod
    def isGreaterTimes(v1, v2, times):
        """
        计算是否大于指定倍数
        :param v1:
        :param v2:
        :param times:指定倍数
        :return:
        """
        if v1 > v2 * times:
            return True
        return False

    @staticmethod
    def isLessTimes(v1, v2, times):
        """
        计算是否小于指定倍数
        :param v1:
        :param v2:
        :param times:指定倍数
        :return:
        """
        if v1 < v2 / times:
            return True
        return False
