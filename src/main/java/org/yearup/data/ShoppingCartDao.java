package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    void addProductToCart(int userId, ShoppingCartItem item);
    void updateProductInCart(int userId, int productId, int quantity);
    void clearCart(int userId);
    boolean contains(int userId, int productId);
}
