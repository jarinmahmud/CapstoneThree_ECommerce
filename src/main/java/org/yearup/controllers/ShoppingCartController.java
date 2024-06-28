package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Convert this class to a REST controller
@RestController
// Add the annotation to make this controller the endpoint for the following URL
@RequestMapping("/cart")
// Add annotation to allow cross site origin requests
@CrossOrigin
// Only logged-in users should have access to these actions
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController {
    // a shopping cart requires
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    // each method in this controller requires a Principal object as a parameter
    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping("")
    public Map<String, Object> getCart(Principal principal) {
        try {
            // Get the currently logged-in username
            String userName = principal.getName();
            // Find database user by username
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // Use the shoppingCartDao to get all items in the cart
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            Map<Integer, ShoppingCartItem> items = cart.getItems();

            Map<String, Object> response = new HashMap<>();
            Map<String, Object> itemsResponse = new HashMap<>();
            BigDecimal total = BigDecimal.ZERO;

            for (ShoppingCartItem item : items.values()) {
                Map<String, Object> itemResponse = new HashMap<>();
                itemResponse.put("product", item.getProduct());
                itemResponse.put("quantity", item.getQuantity());
                itemResponse.put("discountPercent", item.getDiscountPercent());
                itemResponse.put("lineTotal", item.getLineTotal());
                itemsResponse.put(String.valueOf(item.getProductId()), itemResponse);
                total = total.add(item.getLineTotal());
            }

            response.put("items", itemsResponse);
            response.put("total", total);

            return response;
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping("/products/{productId}")
    public void addProductToCart(@PathVariable int productId, Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            Product product = productDao.getById(productId);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
            }

            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(1); // default quantity

            if (shoppingCartDao.contains(userId, productId)) {
                shoppingCartDao.updateProductInCart(userId, productId, shoppingCartDao.getByUserId(userId).getItems().size() + 1);
            } else {
                shoppingCartDao.addProductToCart(userId, item);
            }
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("/products/{productId}")
    public void updateProductInCart(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            if (!shoppingCartDao.contains(userId, productId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not in cart");
            }

            shoppingCartDao.updateProductInCart(userId, productId, item.getQuantity());
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping("")
    public void clearCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.clearCart(userId);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
