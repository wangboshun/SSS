package com.wbs.iot.model.onenet;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WBS
 * @date 2023/2/23 17:05
 * @desciption Datastreams
 */
public class DeviceDataResult implements Serializable {

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


    public class Data implements Serializable {

        private List<Devices> devices;

        public List<Devices> getDevices() {
            return devices;
        }

        public void setDevices(List<Devices> devices) {
            this.devices = devices;
        }

    }


    public class Datastreams implements Serializable {

        private LocalDateTime at;
        private String id;
        private String value;

        public LocalDateTime getAt() {
            return at;
        }

        public void setAt(LocalDateTime at) {
            this.at = at;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public class Data {

            private List<Devices> devices;

            public List<Devices> getDevices() {
                return devices;
            }

            public void setDevices(List<Devices> devices) {
                this.devices = devices;
            }

        }

    }

    public class Devices implements Serializable {

        private String title;
        private String id;
        private List<Datastreams> datastreams;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Datastreams> getDatastreams() {
            return datastreams;
        }

        public void setDatastreams(List<Datastreams> datastreams) {
            this.datastreams = datastreams;
        }

    }
}





