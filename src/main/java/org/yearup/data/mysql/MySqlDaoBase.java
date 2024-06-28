package org.yearup.data.mysql;

import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class MySqlDaoBase
{
    private DataSource dataSource;

    public MySqlDaoBase(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    protected Connection getConnection() throws SQLException
    {
        return dataSource.getConnection();
    }

    public abstract void addProductToCart(int userId, ShoppingCartItem item);

    public abstract void updateProductInCart(int userId, int productId, int quantity);

    public abstract void clearCart(int userId);

    public abstract boolean contains(int userId, int productId);
}