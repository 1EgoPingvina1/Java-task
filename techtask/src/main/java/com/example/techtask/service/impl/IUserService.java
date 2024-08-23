package com.example.techtask.service.impl;

import com.example.techtask.model.Order;
import com.example.techtask.model.User;
import com.example.techtask.model.enumiration.OrderStatus;
import com.example.techtask.model.enumiration.UserStatus;
import com.example.techtask.service.UserService;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Service
public class IUserService implements UserService {

    private Connection connection;
    public IUserService(){
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/techtask","techtask","techtask");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public User findUser() {
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT u.*\n" +
                    "FROM techtask.users u\n" +
                    "INNER JOIN techtask.orders o ON u.id = o.user_id\n" +
                    "WHERE o.created_at BETWEEN '2003-01-01' AND '2003-12-31'\n" +
                    "GROUP BY u.id, u.user_status, u.email\n" +
                    "ORDER BY SUM(o.price * o.quantity) DESC\n" +
                    "LIMIT 1");
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                User user = new User(
                resultSet.getInt("id"),
                resultSet.getString("email"),
                UserStatus.valueOf(resultSet.getString("user_status")));
                return user;
            }
            else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findUsers() {
        try (PreparedStatement statement = connection.prepareStatement("""
        SELECT 
            u.id,
            u.email,
            u.user_status
        FROM 
            techtask.users u
        WHERE 
            u.id IN (
                SELECT 
                    o.user_id
                FROM 
                    techtask.orders o
                WHERE 
                    o.order_status = 'PAID' AND o.created_at BETWEEN '2010-01-01' AND '2010-12-31'
            )
    """)) {
            ResultSet resultSet = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        UserStatus.valueOf(resultSet.getString("user_status"))
                );
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
