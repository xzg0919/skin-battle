package com.tzj.collect.api.iot.param;

import com.tzj.collect.entity.Category;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author sgmark
 * @create 2019-03-30 14:31
 **/
public class IotParamBean {

    private String memberId;//会员id

    private String equipmentCode;//设备编号

    private String orderNo; //订单编号

    private BigDecimal sumPrice;//总价

    private List<ParentList> parentLists;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public List<ParentList> getParentLists() {
        return parentLists;
    }

    public void setParentLists(List<ParentList> parentLists) {
        this.parentLists = parentLists;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(BigDecimal sumPrice) {
        this.sumPrice = sumPrice;
    }

    public static class  ParentList {
        private Category.ParentType parentName;

        private List<ItemList> itemList;

       public Category.ParentType getParentName() {
           return parentName;
       }

       public void setParentName(Category.ParentType parentName) {
           this.parentName = parentName;
       }

       public List<ItemList> getItemList() {
            return itemList;
        }

        public void setItemList(List<ItemList> itemList) {
            this.itemList = itemList;
        }
    }

   public static class ItemList{
        private Category.SecondType name;

        private float quantity;//量

        private String unit;//单位

        private float price;//单价

       public Category.SecondType getName() {
           return name;
       }

       public void setName(Category.SecondType name) {
           this.name = name;
       }

       public float getQuantity() {
            return quantity;
        }

        public void setQuantity(float quantity) {
            this.quantity = quantity;
        }

       public String getUnit() {
           return unit;
       }

       public void setUnit(String unit) {
           this.unit = unit;
       }

       public float getPrice() {
           return price;
       }

       public void setPrice(float price) {
           this.price = price;
       }
   }



}
