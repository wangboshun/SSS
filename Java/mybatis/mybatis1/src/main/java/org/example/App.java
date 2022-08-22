package org.example;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.example.dao.UserInfo2Mapper;
import org.example.dao.UserInfo3Mapper;
import org.example.dao.UserInfoMapper;
import org.example.model.UserInfo;
import org.example.model.UserInfo2;
import org.example.model.UserInfo3;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        Test14();
    }

    public static void Test1() {
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        UserInfoMapper userInfoDao = sqlsession.getMapper(UserInfoMapper.class);
        List<UserInfo> allUserInfo = userInfoDao.getAllUserInfo();
        System.out.println(allUserInfo);
    }

    public static void Test2() {
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        List<UserInfo> allUserInfo = sqlsession.selectList("org.example.dao.UserInfoDao.getAllUserInfo");
        System.out.println(allUserInfo);
    }

    public static void Test3() {
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        UserInfoMapper userInfoDao = sqlsession.getMapper(UserInfoMapper.class);
        UserInfo u = userInfoDao.getUserInfoById("1");
        System.out.println(u);
    }

    public static void Test4() {
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        UserInfoMapper userInfoDao = sqlsession.getMapper(UserInfoMapper.class);
        UserInfo u = new UserInfo();
        u.setId("3");
        u.setName("张三");
        u.setAge(20);
        u.setTM(new Date());
        int i = userInfoDao.insertUserInfo(u);
        sqlsession.commit();
        System.out.println(i);
    }

    public static void Test5() {
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        UserInfoMapper userInfoDao = sqlsession.getMapper(UserInfoMapper.class);
//        UserInfo u = userInfoDao.getUserInfoById("3");
        UserInfo u = new UserInfo();
        u.setId("3");
        u.setName("李四");
        u.setAge(30);
        u.setTM(new Date());
        int i = userInfoDao.updateUserInfo(u);
        sqlsession.commit();
        System.out.println(i);
    }

    public static void Test6() {
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        UserInfoMapper userInfoDao = sqlsession.getMapper(UserInfoMapper.class);
        int i = userInfoDao.deleteUserInfo("3");
        sqlsession.commit();
        System.out.println(i);
    }

    //传输map参数
    public static void Test7() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userid", "2");  //随便定义
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        UserInfoMapper userInfoDao = sqlsession.getMapper(UserInfoMapper.class);
        UserInfo u = userInfoDao.getUserInfoByMap(map);
        userInfoDao.getUserInfoByMap(null);
    }

    //like查询
    public static void Test8() {
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        UserInfoMapper userInfoDao = sqlsession.getMapper(UserInfoMapper.class);
        List<UserInfo> allUserInfo = userInfoDao.getUserInfoByLike("w");
        System.out.println(allUserInfo);
    }

    //结果集映射，适用于类属性和数据库字段不一致情况
    public static void Test9() {
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        UserInfo2Mapper userInfoDao = sqlsession.getMapper(UserInfo2Mapper.class);
        List<UserInfo2> allUserInfo = userInfoDao.getAllUserInfo();
        System.out.println(allUserInfo);
    }

    //RowBounds分页，类似于limit
    public static void Test10() {
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        RowBounds rowBounds = new RowBounds(1, 2);
        List<UserInfo> allUserInfo = sqlsession.selectList("org.example.dao.UserInfoMapper.getUserInfoByRowBounds", null, rowBounds);
        System.out.println(allUserInfo);
    }

    //注解查询,去掉了xml文件
    public static void Test11() {
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        UserInfo3Mapper userInfoDao = sqlsession.getMapper(UserInfo3Mapper.class);
        List<UserInfo2> allUserInfo = userInfoDao.getAllUserInfo();
        System.out.println(allUserInfo);
    }

    //Lombok使用测试
    public static void Test12() {
        UserInfo3 u = new UserInfo3();
        u.setAge(123);
        u.toString();
        System.out.println(u);
    }

    //where查询与一级缓存
    //一级缓存：也叫本地缓存，数据库同一次会话期间查询到的数据会放在本地缓存中
    public static void Test13() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", "1");
        map.put("name", "wbs");
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        UserInfoMapper userInfoDao = sqlsession.getMapper(UserInfoMapper.class);
        UserInfo u1 = userInfoDao.getUserInfoByNameOrId(map);  //这里会查一次sql
        UserInfo u2 = userInfoDao.getUserInfoByNameOrId(map);  //这里不会查sql
        UserInfo u3 = userInfoDao.getUserInfoByNameOrId(map);  //这里也不会查sql
    }

    //二级缓存
    //1、在mybatis.xml下添加 <setting name="cacheEnabled" value="true"/>
    //2、在相关联的mapper下面添加  <cache eviction="FIFO" flushInterval="60000" size="512" readOnly="true"/>
    public static void Test14() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", "1");
        map.put("name", "wbs");
        SqlSession sqlsession = mybatis_hepler.getSqlsession();
        UserInfoMapper userInfoDao1 = sqlsession.getMapper(UserInfoMapper.class);
        UserInfoMapper userInfoDao2 = sqlsession.getMapper(UserInfoMapper.class);
        UserInfo u1_1 = userInfoDao1.getUserInfoByNameOrId(map);  //这里会查一次sql
        UserInfo u1_2 = userInfoDao1.getUserInfoByNameOrId(map);  //这里不会查sql
        UserInfo u2_1 = userInfoDao2.getUserInfoByNameOrId(map);  //这里不会查sql
        UserInfo u2_2 = userInfoDao2.getUserInfoByNameOrId(map);  //这里不会查sql
    }
}
