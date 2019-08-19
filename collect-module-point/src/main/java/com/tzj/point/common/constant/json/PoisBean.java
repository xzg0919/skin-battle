package com.tzj.point.common.constant.json;

public class PoisBean implements Comparable<PoisBean>{
    /**
     * id : B00156D7T3
     * name : 同乐坊
     * type : 商务住宅;产业园区;产业园区
     * tel : 021-62581475;021-52131729
     * direction : Center
     * distance : 0
     * location : 121.442891,31.236803
     * address : 海防路555号
     * poiweight : 0.437374
     * businessarea : 曹家渡
     */

    private String id;
    private String name;
    private String type;
    private String tel;
    private String direction;
    private double distance;
    private String location;
    private String address;
    private String poiweight;
    private String businessarea;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPoiweight() {
        return poiweight;
    }

    public void setPoiweight(String poiweight) {
        this.poiweight = poiweight;
    }

    public String getBusinessarea() {
        return businessarea;
    }

    public void setBusinessarea(String businessarea) {
        this.businessarea = businessarea;
    }

    @Override
    public int compareTo(PoisBean o) {
        double d=this.getDistance()-o.getDistance();
        return (int)d;
    }
}
