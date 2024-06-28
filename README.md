## CapstonThree_ECommerce
## Purpose:
Testing with API an e-commerce platform naming easyshop. It features: 
- administrative controls
- users registration
- managing profiles 
- browsing products
- managing shopping carts
- processing orders etc.

## Flow of the project:
The project foundation was given. The project follows following flow of classes:

- EasyShopApplication.java: Entry point


- Controllers:
AuthenticationController.java: Manages authentication roles. 
CategoriesController.java: Manages product categories.
ProductsController.java: Manages product components.
ProfileController.java: Manages user profiles.
ShoppingCartController.java: Manages the shopping cart related operations.
data


- Data:
CategoryDao.java: Interface for category data.
ProductDao.java: Interface for product data.
ProfileDao.java: Interface for profile data.
UserDao.java: Interface for user data.
ShoppingCartDao.java: Interface for shopping cart data.


- Mysql:
MySqlCategoryDao.java: MySQL implementation of CategoryDao.
MySqlDaoBase: MySQL implementation of Base.
MySqlProductDao.java: MySQL implementation of ProductDao.
MySqlProfileDao.java: MySQL implementation of ProfileDao.
MySqlShoppingCartDao.java: MySQL implementation of ShoppingCartDao.
MySqlUserDao.java: MySQL implementation of UserDao.


- Models:
There are authentication file for authentication purposes. Also it includes:

    Category.java
    
    Product.java
    
    Profile.java
    
    ShoppingCart.java
    
    ShoppingCartItem.java
    
    User.java

#### Besides all of these the project contains security files.

## Developer Notes:
As it is an backend project that interacts with frontend which does not includde that much visibility like front-end. 
Learning new tools and fixing bugs were not easy but an interesting experience indeed.

~created by Jarin Mahmud~
