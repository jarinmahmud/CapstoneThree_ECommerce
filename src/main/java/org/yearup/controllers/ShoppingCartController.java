package org.yearup.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;
import java.security.Principal;


@RestController
@RequestMapping("cart")
@CrossOrigin
@Secured({"ROLE_ADMIN", "ROLE_USER"})
public class ShoppingCartController
{

    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }


    @GetMapping("")
    public ShoppingCart getCart(Principal principal)
    {
        try
        {

            String userName = principal.getName();

            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping("products/{product_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCart addItemToCart(Principal principal, @PathVariable int product_id) {
        try
        {
            // login username
            String userName = principal.getName();

            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            ShoppingCart shoppingCart = getCart(principal);


            shoppingCartDao.addItemToCart(userId, product_id);

        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
        return getCart(principal);
    }

    @PutMapping("products/{product_id}")
    public int updateItemInCart(Principal principal, @PathVariable int product_id) {
        try
        {

            String userName = principal.getName();

            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.updateItemInCart(userId, product_id);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping("")
    public ShoppingCart deleteCart(Principal principal) {
        try
        {

            String userName = principal.getName();

            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.deleteCart(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
        return getCart(principal);
    }

}