package com.wbs.iot.application;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
import java.time.LocalDateTime;
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
    private final IotInterface aliApplication;
    private final IotInterface huaWeiApplication;
    private final IotInterface oneNetApplication;
    private final IotInterface ctWingApplication;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DataSourceFactory dataSourceFactory;
    private DataSource dataSource;
    private final Environment environment;
    private static final String RELATE_TABLE = "zny_eqcloudeq_b";
    private static final String DATA_TABLE = "iot_data";
    private static final String CONFIG_TABLE = "zny_eqcloud_b";

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
            logger.error("------init error------", e);
        }
    }

    /**
     * 获取配置
     *
     * @param cfg
     * @param tp
     * @return
     */
    private Properties getConfig(String cfg, String tp) {
        JSONObject jsonObject = JSONUtil.parseObj(cfg);
        Properties properties = new Properties();
        switch (tp) {
            case "ali":
                properties.setProperty("accessKeyId", jsonObject.getStr("accessKeyId"));
                properties.setProperty("accessKeySecret", jsonObject.getStr("accessKeySecret"));
                properties.setProperty("endpoint", jsonObject.getStr("endpoint"));
                break;
            case "huawei":
                properties.setProperty("accessKeyId", jsonObject.getStr("accessKeyId"));
                properties.setProperty("secretAccessKey", jsonObject.getStr("secretAccessKey"));
                properties.setProperty("region", jsonObject.getStr("region"));
                break;
            case "ctwing":
                properties.setProperty("appKey", jsonObject.getStr("appKey"));
                properties.setProperty("appSecret", jsonObject.getStr("appSecret"));
                break;
            case "onenet":
                properties.setProperty("masterKey", jsonObject.getStr("masterKey"));
                break;
            default:
                break;
        }
        return properties;
    }

    /**
     * 获取本地对应设备
     *
     * @param deviceId
     * @param tp
     * @return
     */
    private String getLocalDevice(String deviceId, String tp) {
        try {
            List<Entity> list = Db.use(dataSource).find(CollUtil.newArrayList("deviceId"), Entity.create(RELATE_TABLE).set("cloudid", deviceId).set("tp", tp));
            if (list.isEmpty()) {
                return null;
            }
            return list.get(0).get("deviceId").toString();
        } catch (SQLException e) {
            logger.error("------getStation error------", e);
            return null;
        }
    }

    /**
     * 是否包含该条设备数据
     *
     * @param deviceId
     * @param time
     * @return
     */
    private boolean hasDeviceData(String deviceId, LocalDateTime time, String tp) {
        try {
            List<Entity> all = Db.use(dataSource).find(CollUtil.newArrayList("deviceId"), Entity.create(DATA_TABLE).set("deviceId", deviceId).set("time", time).set("tp", tp));
            if (!all.isEmpty()) {
                return true;
            }
        } catch (SQLException e) {
            logger.error("------hasDeviceData error------", e);
            return false;
        }
        return false;
    }

    /**
     * 插入设备数据
     *
     * @param list
     * @param tp
     */
    private void insertData(List<DeviceDataModel> list, String tp) {
        try {
            List<Entity> listData = new ArrayList<>();
            for (DeviceDataModel item : list) {
                //如果有这条数据，跳过
                if (hasDeviceData(item.getDeviceId(), item.getTime(), tp)) {
                    continue;
                }
                Entity model = new Entity(DATA_TABLE);
                model.set("tp", tp);
                model.set("name", item.getName());
                model.set("value", item.getValue());
                model.set("time", item.getTime());
                model.set("deviceId", item.getDeviceId());
                model.set("property", item.getProperty());
                String s = getLocalDevice(item.getDeviceId(), tp);
                if (StrUtil.isNotBlank(s)) {
                    model.set("station", s);
                }
                listData.add(model);
            }
            Db.use(dataSource).insert(listData);
        } catch (Exception e) {
            logger.error("------insertData error------", e);
        }
    }

    /**
     * 插入设备
     *
     * @param list
     * @param tp
     */
    private void insertDevice(List<DeviceInfoModel> list, String tp) {
        try {
            List<Entity> listData = new ArrayList<>();
            for (DeviceInfoModel item : list) {
                List<Entity> all = Db.use(dataSource).find(CollUtil.newArrayList("deviceId"), Entity.create(RELATE_TABLE).set("cloudid", item.getId()));
                if (!all.isEmpty()) {
                    continue;
                }
                Entity model = new Entity(RELATE_TABLE);
                model.set("tp", tp);
                model.set("cloudid", item.getId());
                model.set("deviceId", "111---" + item.getId());
                listData.add(model);
            }
            Db.use(dataSource).insert(listData);
        } catch (Exception e) {
            logger.error("------insertDevice error------", e);
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void ali_schedule() {
        List<DeviceDataModel> list = new ArrayList<>();
        try {
            List<Entity> all = Db.use(dataSource).find(CollUtil.newArrayList("cfg"), Entity.create(CONFIG_TABLE).set("tp", "ali"));
            for (Entity entity : all) {
                String cfg = entity.get("cfg").toString();
                Properties properties = getConfig(cfg, "ali");
                aliApplication.config(properties);
                List<ProductInfoModel> productList = aliApplication.getProductList();
                for (ProductInfoModel productInfo : productList) {
                    List<DeviceInfoModel> deviceList = aliApplication.getDeviceList(productInfo);
                    insertDevice(deviceList, "ali");
                    for (DeviceInfoModel deviceInfoModel : deviceList) {
                        List<DeviceDataModel> deviceData = aliApplication.getDeviceData(deviceInfoModel);
                        list.addAll(deviceData);
                    }
                }

                if (!list.isEmpty()) {
                    insertData(list, "ali");
                }
            }
        } catch (Exception e) {
            logger.error("------ali_schedule error------", e);
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void huawei_schedule() {
        List<DeviceDataModel> list = new ArrayList<>();
        try {
            List<Entity> all = Db.use(dataSource).find(CollUtil.newArrayList("cfg"), Entity.create(CONFIG_TABLE).set("tp", "huawei"));
            for (Entity entity : all) {
                String cfg = entity.get("cfg").toString();
                Properties properties = getConfig(cfg, "huawei");
                huaWeiApplication.config(properties);
                List<ProductInfoModel> productList = huaWeiApplication.getProductList();
                for (ProductInfoModel productInfo : productList) {
                    List<DeviceInfoModel> deviceList = huaWeiApplication.getDeviceList(productInfo);
                    insertDevice(deviceList, "huawei");
                    for (DeviceInfoModel deviceInfoModel : deviceList) {
                        List<DeviceDataModel> deviceData = huaWeiApplication.getDeviceData(deviceInfoModel);
                        list.addAll(deviceData);
                    }
                }
                if (!list.isEmpty()) {
                    insertData(list, "huawei");
                }
            }
        } catch (Exception e) {
            logger.error("------huawei_schedule error------", e);
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void ctwing_schedule() {
        List<DeviceDataModel> list = new ArrayList<>();
        try {
            List<Entity> all = Db.use(dataSource).find(CollUtil.newArrayList("cfg"), Entity.create(CONFIG_TABLE).set("tp", "ctwing"));
            for (Entity entity : all) {
                String cfg = entity.get("cfg").toString();
                Properties properties = getConfig(cfg, "ctwing");
                ctWingApplication.config(properties);
                List<ProductInfoModel> productList = ctWingApplication.getProductList();
                for (ProductInfoModel productInfo : productList) {
                    List<DeviceInfoModel> deviceList = ctWingApplication.getDeviceList(productInfo);
                    insertDevice(deviceList, "ctwing");
                    for (DeviceInfoModel deviceInfoModel : deviceList) {
                        List<DeviceDataModel> deviceData = ctWingApplication.getDeviceData(deviceInfoModel);
                        list.addAll(deviceData);
                    }
                }
                if (!list.isEmpty()) {
                    insertData(list, "ctwing");
                }
            }
        } catch (Exception e) {
            logger.error("------ctwing_schedule error------", e);
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void onenet_schedule() {
        List<DeviceDataModel> list = new ArrayList<>();
        try {
            List<Entity> all = Db.use(dataSource).find(CollUtil.newArrayList("cfg"), Entity.create(CONFIG_TABLE).set("tp", "onenet"));
            for (Entity entity : all) {
                String cfg = entity.get("cfg").toString();
                Properties properties = getConfig(cfg, "onenet");
                oneNetApplication.config(properties);
                List<DeviceInfoModel> deviceList = oneNetApplication.getDeviceList(new ProductInfoModel());
                insertDevice(deviceList, "onenet");
                for (DeviceInfoModel deviceInfoModel : deviceList) {
                    List<DeviceDataModel> deviceData = oneNetApplication.getDeviceData(deviceInfoModel);
                    list.addAll(deviceData);
                }
                if (!list.isEmpty()) {
                    insertData(list, "onenet");
                }
            }
        } catch (Exception e) {
            logger.error("------onenet_schedule error------", e);
        }
    }
}
