package com.example.techtask.service.impl;

import com.example.techtask.model.Order;
import com.example.techtask.model.enumiration.OrderStatus;
import com.example.techtask.service.OrderService;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class IOrderService implements OrderService {

    private Connection connection;
    public IOrderService(){
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/techtask","techtask","techtask");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order findOrder()
    {
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * From orders o WHERE o.quantity > 1 ORDER BY o.created_at DESC LIMIT 1");
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Order order = new Order(
                        resultSet.getInt("id"),
                        resultSet.getString("product_name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("user_id"),
                        resultSet.getTimestamp("created_at").toLocalDateTime(),
                        OrderStatus.valueOf(resultSet.getString("order_status"))
                );
                return order;
            }
            else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findOrders() {
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT *\n" +
                    "FROM orders\n" +
                    "INNER JOIN techtask.users u on u.id = orders.user_id\n" +
                    "WHERE user_status = 'ACTIVE'\n" +
                    "ORDER BY created_at DESC");
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while(resultSet.next()){
                Order order = new Order(
                        resultSet.getInt("id"),
                        resultSet.getString("product_name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("user_id"),
                        resultSet.getTimestamp("created_at").toLocalDateTime(),
                        OrderStatus.valueOf(resultSet.getString("order_status"))
                );
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
