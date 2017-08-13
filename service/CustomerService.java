package com.chy.chapter2.service;

import com.chy.chapter2.model.Customer;
import com.chy.chapter2.utils.DataBaseHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 提供客户数据服务
 * Created by yang on 2017/7/15.
 */
public class CustomerService {

    /**
     * 获取客户列表
     * @param customerBean
     * @return
     */
    public List<Customer> getCustomerList(Customer customerBean){
        // TODO
        Connection conn = DataBaseHelper.getConnection();
        String sql = "select * from customer";
        List<Map<String,Object>> maps = DataBaseHelper.queryList(conn,sql);
        List<Customer> list = DataBaseHelper.changeMapListToEntityList(Customer.class,maps);
        return list;
    }

    /**
     * 获取单个客户
     * @param id
     * @return
     */
    public Customer getCustomer(long id){
        //TODO
        Connection conn = DataBaseHelper.getConnection();
        String sql = "select * from customer where id='"+id+"'";
        Customer customer = DataBaseHelper.queryEntity(Customer.class,conn,sql);
        return customer;
    }

    /**
     * 创建客户
     * @param fieldMap
     * @return
     */
    public boolean createCustomer(Map<String,Object> fieldMap){
        //TODO
        Connection conn = DataBaseHelper.getConnection();
        return DataBaseHelper.insertEntity(Customer.class,conn,fieldMap);
    }

    /**
     * 更新客户
     * @param id
     * @param fieldMap
     * @return
     */
    public boolean updateCustomer(long id ,Map<String,Object> fieldMap){
        //TODO
        Connection conn = DataBaseHelper.getConnection();
        return DataBaseHelper.updateEntity(Customer.class,conn,fieldMap);
    }

    /**
     * 删除客户
     * @param fieldMap
     * @return
     */
    public boolean deleteCustomer(Map<String,Object> fieldMap){
        //TODO
        Connection conn = DataBaseHelper.getConnection();
        return DataBaseHelper.deleteEntity(Customer.class,conn,fieldMap);
    }
}
