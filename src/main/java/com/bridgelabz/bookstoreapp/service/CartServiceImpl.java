package com.bridgelabz.bookstoreapp.service;

import com.bridgelabz.bookstoreapp.Exception.BookStoreException;
import com.bridgelabz.bookstoreapp.dto.CartDto;
import com.bridgelabz.bookstoreapp.dto.CartQtyDto;
import com.bridgelabz.bookstoreapp.dto.EmailDto;
import com.bridgelabz.bookstoreapp.entity.Cart;
import com.bridgelabz.bookstoreapp.entity.OrderNumber;
import com.bridgelabz.bookstoreapp.entity.User;
import com.bridgelabz.bookstoreapp.repository.BookStoreRepository;
import com.bridgelabz.bookstoreapp.repository.CartRepository;
import com.bridgelabz.bookstoreapp.repository.OrderNumberRepository;
import com.bridgelabz.bookstoreapp.repository.UserRepository;
import com.bridgelabz.bookstoreapp.utility.ConverterService;
import com.bridgelabz.bookstoreapp.utility.JwtUtils;
import com.bridgelabz.bookstoreapp.utility.RabbitMq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@PropertySource("classpath:message.properties")
public class CartServiceImpl implements ICartService {

    private static final int ORDER_ID_NUM_ADD = 100000;

    @Autowired
    private ConverterService converterService;

    @Autowired
    private BookStoreRepository bookStoreRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailDto emailDto;

    @Autowired
    private RabbitMq rabbitMq;

    @Autowired
    private OrderNumberRepository orderNumberRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private Environment environment;

    @Override
    public String addToCart(CartDto cartDto, String token) throws BookStoreException {
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            Cart cart = converterService.convertToCartEntity(cartDto);
            cart.setUsername(username);
            if (cartRepository.existsCartByUsername(username) && cartRepository.existsCartByBookId(cart.getBookId()))
                cartRepository.deleteCartByBookIdAndUsername(cart.getBookId(), cart.getUsername());
            cartRepository.save(cart);
            return environment.getProperty("ADDED_TO_CART");
        } else
            throw new BookStoreException(BookStoreException.ExceptionType.JWT_NOT_VALID, environment.getProperty("JWT_NOT_VALID"));
    }

    @Override
    public String removeFromCart(CartDto cartDto, String token) throws BookStoreException {
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            Cart cart = converterService.convertToCartEntity(cartDto);
            cart.setUsername(username);
            cartRepository.deleteCartByBookIdAndUsername(cart.getBookId(), cart.getUsername());
            return environment.getProperty("REMOVED_FROM_CART");
        } else
            throw new BookStoreException(BookStoreException.ExceptionType.JWT_NOT_VALID, environment.getProperty("JWT_NOT_VALID"));
    }

    @Override
    public List<CartQtyDto> getCartBooks(String token) {
        List<CartQtyDto> cartQtyDto = new ArrayList<>();
        try {
            if (jwtUtils.validateJwtToken(token)) {
                String username = jwtUtils.getUserNameFromJwtToken(token);
                List<Cart> allByUsername = cartRepository.findAllByUsername(username);
                for (Cart cart : allByUsername) {
                    if (cart.getBookQuantity() == 0)
                        cartRepository.deleteCartByBookIdAndUsername(cart.getBookId(), cart.getUsername());
                    else {
                        cartQtyDto.add(new CartQtyDto(bookStoreRepository.findById(cart.getBookId()).getId(),
                                bookStoreRepository.findById(cart.getBookId()).getAuthor(),
                                bookStoreRepository.findById(cart.getBookId()).getNameOfBook(),
                                bookStoreRepository.findById(cart.getBookId()).getPicPath(),
                                bookStoreRepository.findById(cart.getBookId()).getPrice(),
                                cartRepository.findByBookIdAndUsername(cart.getBookId(), cart.getUsername()).getBookQuantity()));
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return cartQtyDto;
    }

    @Override
    public Integer getOrderId(String token) throws BookStoreException {
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            List<CartQtyDto> cartBooks = getCartBooks(token);
            for (CartQtyDto cartQtyDto : cartBooks) {
                int id = cartQtyDto.getId();
                int userBookQuantity = cartQtyDto.getBookQuantity();
                int storeQuantity = bookStoreRepository.findById(id).getQuantity();
                if (userBookQuantity <= storeQuantity) {
                    bookStoreRepository.findById(id).setQuantity(storeQuantity - userBookQuantity);
                } else {
                    throw new BookStoreException(BookStoreException.ExceptionType.QUANTITY_EXCEEDED, environment.getProperty("QUANTITY_EXCEEDED"));
                }
            }
            for (CartQtyDto books : cartBooks) {
                cartRepository.deleteCartByBookIdAndUsername(books.getId(), username);
            }
            Optional<User> byUsername = userRepository.findByUsername(username);
            String email = byUsername.get().getEmail();
            OrderNumber orderNumberNew = new OrderNumber();
            OrderNumber orderNumber = orderNumberRepository.findFirstByOrderByIdDesc();
            if (orderNumber == null) {
                orderNumberNew.setUsername(username);
                orderNumberNew.setOrderId(ORDER_ID_NUM_ADD);
                orderNumberRepository.save(orderNumberNew);
                sendEmailWithOrderDetails(email, ORDER_ID_NUM_ADD);
                return orderNumberNew.getOrderId();
            }
            orderNumberNew.setUsername(username);
            orderNumberNew.setOrderId(ORDER_ID_NUM_ADD + orderNumber.getId());
            orderNumberRepository.save(orderNumberNew);
            sendEmailWithOrderDetails(email, ORDER_ID_NUM_ADD + orderNumber.getId());
            return orderNumberNew.getOrderId();
        } else
            throw new BookStoreException(BookStoreException.ExceptionType.JWT_NOT_VALID, environment.getProperty("JWT_NOT_VALID"));
    }

    @Override
    public Integer getPlaceOrderId(String token) throws BookStoreException {
        if (jwtUtils.validateJwtToken(token)) {
            OrderNumber firstByOrderByIdDesc = orderNumberRepository.findFirstByOrderByIdDesc();
            return firstByOrderByIdDesc.getOrderId();
        }else
            throw new BookStoreException(BookStoreException.ExceptionType.JWT_NOT_VALID, environment.getProperty("JWT_NOT_VALID"));
    }

    private void sendEmailWithOrderDetails(String email, int orderId) {
        emailDto.setTo(email);
        emailDto.setFrom("preranapshinde3009@gmail.com");
        emailDto.setSubject(environment.getProperty("ORDER_PLACED"));
        emailDto.setBody("Thank you for placing order with us, your order id is " + orderId + " (: Happy reading :)");
        rabbitMq.sendMessageToQueue(emailDto);
    }
}

