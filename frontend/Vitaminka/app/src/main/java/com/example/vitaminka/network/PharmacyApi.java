package com.example.vitaminka.network;
import com.example.vitaminka.model.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface PharmacyApi {

    // ── Аутентификация ────────────────────────────────────────────────────
    @POST("login/auth")
    Call<AuthResponse> login(@Body AuthRequest request);

    // ── Пользователи ──────────────────────────────────────────────────────
    @GET("users")
    Call<List<User>> getUsers();

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") int id);

    @POST("users")
    Call<User> createUser(@Body CreateUserRequest request);

    // ── Роли ──────────────────────────────────────────────────────────────
    @GET("roles")
    Call<List<Role>> getRoles();

    // ── Скидки ────────────────────────────────────────────────────────────
    @GET("users/{id}/discount")
    Call<List<Discount>> getUserDiscounts(@Path("id") int userId);

    // ── Препараты ─────────────────────────────────────────────────────────
    /** Список препаратов с необязательными фильтрами. */
    @GET("drugs")
    Call<List<Drug>> getDrugs(
            @Query("category") String category,
            @Query("typeId")     String typeId
    );

    @GET("drugs/{id}")
    Call<DrugDetail> getDrugById(@Path("id") int id);

    // ── Склад и партии ────────────────────────────────────────────────────
    @GET("batches/{id}")
    Call<Batch> getBatchById(@Path("id") int id);

    @GET("batches")
    Call<List<Batch>> getBatches();

    @GET("stock")
    Call<List<StockItem>> getStock(@Query("drugId") int drugId);

    // ── Корзина ───────────────────────────────────────────────────────────
    @GET("cart")
    Call<List<Cart>> getCartsByUser(@Query("userId") int userId);

    @POST("cart")
    Call<Cart> createCart(@Body CreateCartRequest request);

    // Элементы корзины (через userId)
    @GET("cart/{userId}/items")
    Call<List<CartItem>> getCartItems(@Path("userId") int userId);

    @POST("cart/{userId}/items")
    Call<Void> addItemToCart(@Path("userId") int userId, @Body AddToCartRequest request);

    @PUT("cart/{userId}/items/{itemId}")
    Call<Void> updateCartItemQuantity(@Path("userId") int userId,
                                      @Path("itemId") int itemId,
                                      @Body UpdateCartItemRequest request);

    @DELETE("cart/{userId}/items/{itemId}")
    Call<Void> removeCartItem(@Path("userId") int userId,
                              @Path("itemId") int itemId);


    // ── Заказы ────────────────────────────────────────────────────────────
    @POST("orders")
    Call<Order> createOrder(@Body CreateOrderRequest request);

    @GET("orders")
    Call<List<Order>> getOrders();

    @GET("orders/{id}")
    Call<Order> getOrderById(@Path("id") int id);

    @PATCH("orders/{id}")
    Call<Order> updateOrderStatus(
            @Path("id")  int id,
            @Body        UpdateOrderRequest request
    );

    // ── Резервации ────────────────────────────────────────────────────────
    @POST("reservations")
    Call<Void> createReservation(@Body ReservationRequest request);

    // ── Поставки ──────────────────────────────────────────────────────────
    @GET("supplies")
    Call<List<Supply>> getSupplies();

    @POST("supplies")
    Call<Supply> createSupply(@Body CreateSupplyRequest request);

    @GET("drugtypes")
    Call<List<DrugType>> getDrugTypes();

    @GET("batches")
    Call<List<Batch>> getBatchesByDrug(@Query("drugId") int drugId);
}