package com.wbs.iot.model.onenet;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WBS
 * @LocalDateTime 2023/2/23 17:16
 * @desciption DeviceListResult
 */
public class DeviceListResult implements Serializable {

    private int errno;
    private Data data;
    private String error;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public class Location {

        private int lat;
        private int lon;

        public int getLat() {
            return lat;
        }

        public void setLat(int lat) {
            this.lat = lat;
        }

        public int getLon() {
            return lon;
        }

        public void setLon(int lon) {
            this.lon = lon;
        }

    }


    public class Devices {

        private String protocol;
        private boolean private1;
        private LocalDateTime create_time;
        private LocalDateTime act_time;
        private LocalDateTime last_login;
        private boolean online;
        private Location location;
        private String id;
        private String auth_info;
        private String title;
        private String desc;
        private List<String> tags;

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public boolean getPrivate1() {
            return private1;
        }

        public void setPrivate1(boolean private1) {
            this.private1 = private1;
        }

        public LocalDateTime getCreate_time() {
            return create_time;
        }

        public void setCreate_time(LocalDateTime create_time) {
            this.create_time = create_time;
        }

        public LocalDateTime getAct_time() {
            return act_time;
        }

        public void setAct_time(LocalDateTime act_time) {
            this.act_time = act_time;
        }

        public LocalDateTime getLast_login() {
            return last_login;
        }

        public void setLast_login(LocalDateTime last_login) {
            this.last_login = last_login;
        }

        public boolean getOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuth_info() {
            return auth_info;
        }

        public void setAuth_info(String auth_info) {
            this.auth_info = auth_info;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

    }

    public class Data {

        private int per_page;
        private List<Devices> devices;
        private int total_count;
        private int page;

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public List<Devices> getDevices() {
            return devices;
        }

        public void setDevices(List<Devices> devices) {
            this.devices = devices;
        }

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

    }

}
