package com.wbs.iot.application;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wbs.common.database.DataSourceFactory;
import com.wbs.common.database.DbTypeEnum;
import com.wbs.iot.model.IotEnum;
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
    private Db currentDb;
    private final Environment environment;
    private static final String RELATE_TABLE = "zny_eqcloudeq_b";
    private static final String DATA_TABLE = "iot_data";
    private static final String CONFIG_TABLE = "zny_eqcloud_b";
    private static final String DEVICE_TABLE = "zny_device_instance";

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
            DataSource dataSource = dataSourceFactory.createDataSource("iot", host, port, username, password, database, DbTypeEnum.MySql);
            currentDb = Db.use(dataSource);
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
    private Properties getConfig(String cfg, IotEnum tp) {
        JSONObject jsonObject = JSONUtil.parseObj(cfg);
        Properties properties = new Properties();
        switch (tp) {
            case Ali:
                properties.setProperty("accessKeyId", jsonObject.getStr("accessKeyId"));
                properties.setProperty("accessKeySecret", jsonObject.getStr("accessKeySecret"));
                properties.setProperty("endpoint", jsonObject.getStr("endpoint"));
                break;
            case HuaWei:
                properties.setProperty("accessKeyId", jsonObject.getStr("accessKeyId"));
                properties.setProperty("secretAccessKey", jsonObject.getStr("secretAccessKey"));
                properties.setProperty("region", jsonObject.getStr("region"));
                break;
            case Ctwing:
                properties.setProperty("appKey", jsonObject.getStr("appKey"));
                properties.setProperty("appSecret", jsonObject.getStr("appSecret"));
                break;
            case OneNet:
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
     * @param cloudId
     * @param tp
     * @return
     */
    private String getLocalDevice(String cloudId, String name, IotEnum tp) {
        try {
            List<Entity> list = currentDb.find(CollUtil.newArrayList("deviceid"), Entity.create(RELATE_TABLE).set("cloudid", cloudId).set("tp", tp.toString()));
            if (list.isEmpty()) {
                return addLocalDevice(cloudId, name, tp);
            }
            return list.get(0).get("deviceid").toString();
        } catch (SQLException e) {
            logger.error("------getStation error------", e);
            return null;
        }
    }

    /**
     * 添加本地设备
     *
     * @param cloudId
     * @param tp
     */
    private String addLocalDevice(String cloudId, String name, IotEnum tp) {
        String deviceId = RandomUtil.randomInt(10, 100) + "";
        try {
            Entity deviceModel = new Entity(DEVICE_TABLE);
            deviceModel.set("id", deviceId);
            deviceModel.set("name", name);
            deviceModel.set("product_id", "CloudProduct");
            deviceModel.set("product_name", "云云对接产品");

            currentDb.insert(deviceModel);

            Entity relateModel = new Entity(RELATE_TABLE);
            relateModel.set("cloudid", cloudId);
            relateModel.set("deviceid", deviceId);
            relateModel.set("tp", tp.toString());
            currentDb.insert(relateModel);

        } catch (Exception e) {
            logger.error("------addLocalDevice error------", e);
        }
        return deviceId;
    }

    /**
     * 是否包含该条设备数据
     *
     * @param deviceId
     * @param time
     * @return
     */
    private boolean hasDeviceData(String deviceId, LocalDateTime time, IotEnum tp) {
        try {
            List<Entity> all = currentDb.find(CollUtil.newArrayList("deviceId"), Entity.create(DATA_TABLE).set("deviceId", deviceId).set("time", time).set("tp", tp.toString()));
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
    private void insertData(List<DeviceDataModel> list, IotEnum tp) {
        try {
            List<Entity> listData = new ArrayList<>();
            for (DeviceDataModel item : list) {
                // 如果有这条数据，跳过
                if (hasDeviceData(item.getDeviceId(), item.getTime(), tp)) {
                    continue;
                }
                Entity model = new Entity(DATA_TABLE);
                model.set("tp", tp.toString());
                model.set("name", item.getName());
                model.set("value", item.getValue());
                model.set("time", item.getTime());
                model.set("deviceId", item.getDeviceId());
                model.set("property", item.getProperty());
                String s = getLocalDevice(item.getDeviceId(), item.getName(), tp);
                if (StrUtil.isNotBlank(s)) {
                    model.set("station", s);
                }
                listData.add(model);
            }
            currentDb.insert(listData);
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
    private void insertDevice(List<DeviceInfoModel> list, IotEnum tp) {
        try {
            for (DeviceInfoModel item : list) {
                List<Entity> all = currentDb.find(CollUtil.newArrayList("deviceId"), Entity.create(RELATE_TABLE).set("cloudid", item.getId()));
                if (!all.isEmpty()) {
                    continue;
                }
                addLocalDevice(item.getId(), item.getName(), tp);
            }
        } catch (Exception e) {
            logger.error("------insertDevice error------", e);
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void ali_schedule() {
        List<DeviceDataModel> list = new ArrayList<>();
        try {
            List<Entity> all = currentDb.find(CollUtil.newArrayList("cfg"), Entity.create(CONFIG_TABLE).set("tp", IotEnum.Ali.toString()));
            for (Entity entity : all) {
                String cfg = entity.get("cfg").toString();
                Properties properties = getConfig(cfg, IotEnum.Ali);
                aliApplication.config(properties);
                List<ProductInfoModel> productList = aliApplication.getProductList();
                for (ProductInfoModel productInfo : productList) {
                    List<DeviceInfoModel> deviceList = aliApplication.getDeviceList(productInfo);
                    insertDevice(deviceList, IotEnum.Ali);
                    for (DeviceInfoModel deviceInfoModel : deviceList) {
                        List<DeviceDataModel> deviceData = aliApplication.getDeviceData(deviceInfoModel);
                        list.addAll(deviceData);
                    }
                }

                if (!list.isEmpty()) {
                    insertData(list, IotEnum.Ali);
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
            List<Entity> all = currentDb.find(CollUtil.newArrayList("cfg"), Entity.create(CONFIG_TABLE).set("tp", IotEnum.HuaWei.toString()));
            for (Entity entity : all) {
                String cfg = entity.get("cfg").toString();
                Properties properties = getConfig(cfg, IotEnum.HuaWei);
                huaWeiApplication.config(properties);
                List<ProductInfoModel> productList = huaWeiApplication.getProductList();
                for (ProductInfoModel productInfo : productList) {
                    List<DeviceInfoModel> deviceList = huaWeiApplication.getDeviceList(productInfo);
                    insertDevice(deviceList, IotEnum.HuaWei);
                    for (DeviceInfoModel deviceInfoModel : deviceList) {
                        List<DeviceDataModel> deviceData = huaWeiApplication.getDeviceData(deviceInfoModel);
                        list.addAll(deviceData);
                    }
                }
                if (!list.isEmpty()) {
                    insertData(list, IotEnum.HuaWei);
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
            List<Entity> all = currentDb.find(CollUtil.newArrayList("cfg"), Entity.create(CONFIG_TABLE).set("tp", IotEnum.Ctwing.toString()));
            for (Entity entity : all) {
                String cfg = entity.get("cfg").toString();
                Properties properties = getConfig(cfg, IotEnum.Ctwing);
                ctWingApplication.config(properties);
                List<ProductInfoModel> productList = ctWingApplication.getProductList();
                for (ProductInfoModel productInfo : productList) {
                    List<DeviceInfoModel> deviceList = ctWingApplication.getDeviceList(productInfo);
                    insertDevice(deviceList, IotEnum.Ctwing);
                    for (DeviceInfoModel deviceInfoModel : deviceList) {
                        List<DeviceDataModel> deviceData = ctWingApplication.getDeviceData(deviceInfoModel);
                        list.addAll(deviceData);
                    }
                }
                if (!list.isEmpty()) {
                    insertData(list, IotEnum.Ctwing);
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
            List<Entity> all = currentDb.find(CollUtil.newArrayList("cfg"), Entity.create(CONFIG_TABLE).set("tp", IotEnum.OneNet.toString()));
            for (Entity entity : all) {
                String cfg = entity.get("cfg").toString();
                Properties properties = getConfig(cfg, IotEnum.OneNet);
                oneNetApplication.config(properties);
                List<DeviceInfoModel> deviceList = oneNetApplication.getDeviceList(new ProductInfoModel());
                insertDevice(deviceList, IotEnum.OneNet);
                for (DeviceInfoModel deviceInfoModel : deviceList) {
                    List<DeviceDataModel> deviceData = oneNetApplication.getDeviceData(deviceInfoModel);
                    list.addAll(deviceData);
                }
                if (!list.isEmpty()) {
                    insertData(list, IotEnum.OneNet);
                }
            }
        } catch (Exception e) {
            logger.error("------onenet_schedule error------", e);
        }
    }
}
