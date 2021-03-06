package info.mike.webstorev1.controllers;

import info.mike.webstorev1.domain.Cart;
import info.mike.webstorev1.service.CartService;
import info.mike.webstorev1.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class CartRestController {

    private final CartService cartService;
    private final ProductService productService;

    public CartRestController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping("/rest/cart")
    public ResponseEntity<Cart> showCart(HttpServletRequest httpServletRequest) {
        String sessionId = httpServletRequest.getSession(true).getId();
        Cart cartBySessionId = cartService.findByCartSessionId(sessionId);
        if (cartBySessionId == null) {
            return new ResponseEntity<Cart>(cartService.createCart(sessionId), HttpStatus.OK);
        }
        return new ResponseEntity<Cart>(cartBySessionId, HttpStatus.OK);
    }

    @PutMapping("/add/{productId}")
    public ResponseEntity<Cart> addProduct(@PathVariable String productId, HttpServletRequest httpServletRequest) {
        Cart cartBySessionId = cartService.findByCartSessionId(httpServletRequest.getSession(true).getId());

        if (cartBySessionId == null) {
            cartBySessionId = cartService.createCart(httpServletRequest.getSession(true).getId());
        }
        Cart savedCart = cartService.addProduct(cartBySessionId, Long.valueOf(productId));
        log.debug("Wywołano addProduct(PUT)");
        return new ResponseEntity<Cart>(savedCart, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Cart> deleteProduct(@PathVariable String productId, HttpServletRequest httpServletRequest) {
        Cart cartBySessionId = cartService.findByCartSessionId(httpServletRequest.getSession(true).getId());

        if (cartBySessionId == null) {
            //ToDo
            //throw new CartNotFoundException();
            return null;
        }
            Cart savedCart = cartService.deleteProduct(cartBySessionId, Long.valueOf(productId));
            log.debug("Wywołano deleteProduct(DELETE)");

        return new ResponseEntity<Cart>(savedCart, HttpStatus.OK);
    }

}
