package com.wbs.iot.application;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.wbs.common.database.DataSourceFactory;
import com.wbs.common.database.DbTypeEnum;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author WBS
 * @date 2023/3/1 15:47
 * @desciption IotSchedule
 */
@Component
public class IotSchedule {
    private final AliApplication aliApplication;
    private final HuaWeiApplication huaWeiApplication;
    private final OneNetApplication oneNetApplication;
    private final CtWingApplication ctWingApplication;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DataSourceFactory dataSourceFactory;
    private DataSource dataSource;
    private final Environment environment;

    public IotSchedule(AliApplication aliApplication, HuaWeiApplication huaWeiApplication, OneNetApplication oneNetApplication, CtWingApplication ctWingApplication, DataSourceFactory dataSourceFactory, Environment environment) {
        this.aliApplication = aliApplication;
        this.huaWeiApplication = huaWeiApplication;
        this.oneNetApplication = oneNetApplication;
        this.ctWingApplication = ctWingApplication;
        this.dataSourceFactory = dataSourceFactory;
        this.environment = environment;
        init();
    }

    private void init() {
        String host = environment.getProperty("iot_db.host");
        int port = Integer.parseInt(environment.getRequiredProperty("iot_db.port"));
        String username = environment.getProperty("iot_db.username");
        String password = environment.getProperty("iot_db.password");
        String database = environment.getProperty("iot_db.database");
        try {
            this.dataSource = dataSourceFactory.createDataSource("iot", host, port, username, password, database, DbTypeEnum.MySql);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void insertData(List<DeviceDataModel> list, int type) {
        try {
            List<Entity> listData = new ArrayList<>();
            for (DeviceDataModel item : list) {
                Entity model = new Entity("iot_data");
                model.set("type", type);
                model.set("name", item.getName());
                model.set("value", item.getValue());
                model.set("time", item.getTime());
                model.set("deviceId", item.getDeviceId());
                model.set("property", item.getProperty());
                listData.add(model);
            }
            int[] insert = Db.use(dataSource).insert(listData);
            System.out.println();
        } catch (Exception e) {

        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void ali_schedule() {
        List<DeviceDataModel> list = new ArrayList<>();
        List<Entity> listData = new ArrayList<>();
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", "LTAImYBSVfLTwohp");
        properties.setProperty("accessKeySecret", "nac27NrdgEllUXGLw0DXeQOg1zUOJ9");
        properties.setProperty("endpoint", "iot.cn-shanghai.aliyuncs.com");
        aliApplication.config(properties);
        List<ProductInfoModel> productList = aliApplication.getProductList();
        for (ProductInfoModel productInfo : productList) {
            List<DeviceInfoModel> deviceList = aliApplication.getDeviceList(productInfo);
            for (DeviceInfoModel deviceInfoModel : deviceList) {
                List<DeviceDataModel> deviceData = aliApplication.getDeviceData(deviceInfoModel);
                list.addAll(deviceData);
            }
        }

        if (list.size() > 0) {
            insertData(list, 1);
            System.out.println();
        }
        logger.info("------ali_schedule------");
        System.out.println("------ali_schedule------");
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void huawei_schedule() {
        List<DeviceDataModel> list = new ArrayList<>();
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", "WPC9R5I3AQAYINPQRAEG");
        properties.setProperty("secretAccessKey", "yyayWWTluTbsF45Q0xWhDPAUZZng8pyxPa1114NW");
        properties.setProperty("region", "cn-north-4");
        huaWeiApplication.config(properties);
        List<ProductInfoModel> productList = huaWeiApplication.getProductList();
        for (ProductInfoModel productInfo : productList) {
            List<DeviceInfoModel> deviceList = huaWeiApplication.getDeviceList(productInfo);
            for (DeviceInfoModel deviceInfoModel : deviceList) {
                List<DeviceDataModel> deviceData = huaWeiApplication.getDeviceData(deviceInfoModel);
                list.addAll(deviceData);
            }
        }
        if (list.size() > 0) {
            insertData(list, 2);
            System.out.println();
        }
        logger.info("------huawei_schedule------");
        System.out.println("------huawei_schedule------");
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void ctwing_schedule() {
        List<DeviceDataModel> list = new ArrayList<>();
        Properties properties = new Properties();
        properties.setProperty("appKey", "Pfg5YFp8xQd");
        properties.setProperty("appSecret", "imkBxvny86");
        ctWingApplication.config(properties);
        List<ProductInfoModel> productList = ctWingApplication.getProductList();
        for (ProductInfoModel productInfo : productList) {
            List<DeviceInfoModel> deviceList = ctWingApplication.getDeviceList(productInfo);
            for (DeviceInfoModel deviceInfoModel : deviceList) {
                List<DeviceDataModel> deviceData = ctWingApplication.getDeviceData(deviceInfoModel);
                list.addAll(deviceData);
            }
        }
        if (list.size() > 0) {
            insertData(list, 3);
            System.out.println();
        }
        logger.info("------ctwing_schedule------");
        System.out.println("------ctwing_schedule------");
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void onenet_schedule() {
        List<DeviceDataModel> list = new ArrayList<>();
        Properties properties = new Properties();
        properties.setProperty("masterKey", "HxC7tM2w7WuJv0pVJ6Y3LYAKsmY=");
        oneNetApplication.config(properties);
        List<DeviceInfoModel> deviceList = oneNetApplication.getDeviceList(new ProductInfoModel());
        for (DeviceInfoModel deviceInfoModel : deviceList) {
            List<DeviceDataModel> deviceData = oneNetApplication.getDeviceData(deviceInfoModel);
            list.addAll(deviceData);
        }
        if (list.size() > 0) {
            insertData(list, 4);
            System.out.println();
        }
        logger.info("------onenet_schedule------");
        System.out.println("------onenet_schedule------");
    }
}
