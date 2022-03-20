package com.example.ordermanagement.Service;

import com.example.ordermanagement.Configuration.LineItemServiceConfiguration;
import com.example.ordermanagement.Exception.ProductAlreadyInLineItemException;
import com.example.ordermanagement.Dtos.CartItemResponseDTO;
import com.example.ordermanagement.Dtos.LineItemRequestDTO;
import com.example.ordermanagement.Dtos.LineItemResponseDTO;
import com.example.ordermanagement.Entity.*;
import com.example.ordermanagement.Exception.ForbiddenException;
import com.example.ordermanagement.Exception.QuantityNotSufficientException;
import com.example.ordermanagement.Factory.LineItemFactory;
import com.example.ordermanagement.Factory.OrderFactory;
import com.example.ordermanagement.Factory.ProductFactory;
import com.example.ordermanagement.Factory.UserFactory;
import com.example.ordermanagement.Repository.LineItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({LineItemServiceConfiguration.class})
class LineItemServiceTest {
    @Autowired
    private LineItemService lineItemService;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private ProductFactory productFactory;

    @Autowired
    private OrderFactory orderFactory;

    @Autowired
    private LineItemFactory lineItemFactory ;
    
    @Autowired
    private LineItemRepository lineItemRepository;

    @Test
    void test_get_success_all_line_item() {
        AppUser user = userFactory.createUser();

        Order order = orderFactory.createOrder(user);

        Product product = productFactory.createProduct();

        LineItem lineItem = lineItemFactory.createLineItem(order, product , product.getQuantity() -5);

        Pageable pageable = PageRequest.of(0 ,10);

        Page<CartItemResponseDTO> response = lineItemService.getAllLineItem(user.getUsername(), pageable);

        assertThat(response.getContent()).isNotEmpty();

        assertThat(response.getContent().get(0).getProductID()).isEqualTo(product.getId());
        assertThat(response.getContent().get(0).getOrderID()).isEqualTo(order.getId());
        assertThat(response.getContent().get(0).getName()).isEqualTo(product.getName());
        assertThat(response.getContent().get(0).getCategory()).isEqualTo(product.getCategory());
        assertThat(response.getContent().get(0).getQuantity()).isEqualTo(lineItem.getQuantity());

    }

    @Test
    void test_save_line_item_with_order_success() {
        AppUser user = userFactory.createUser();

        Order order = orderFactory.createOrder(user);

        Product product = productFactory.createProduct();

        LineItemRequestDTO itemRequestDTO = new LineItemRequestDTO();
        itemRequestDTO.setOrderID(order.getId());
        itemRequestDTO.setProductID(product.getId());
        itemRequestDTO.setQuantity(product.getQuantity() - 5);

        LineItemResponseDTO response = lineItemService.saveLineItem(user.getUsername(), itemRequestDTO);

        assertThat(response.getQuantity()).isEqualTo(itemRequestDTO.getQuantity());
        assertThat(response.getLineItemKey().getOrderID()).isEqualTo(itemRequestDTO.getOrderID());
        assertThat(response.getLineItemKey().getProductID()).isEqualTo(itemRequestDTO.getProductID());

    }

    @Test
    void test_save_line_item_without_order_success() {
        AppUser user = userFactory.createUser();

        Product product = productFactory.createProduct();

        LineItemRequestDTO itemRequestDTO = new LineItemRequestDTO();
        itemRequestDTO.setProductID(product.getId());
        itemRequestDTO.setQuantity(product.getQuantity() - 5);

        LineItemResponseDTO response = lineItemService.saveLineItem(user.getUsername(), itemRequestDTO);

        assertThat(response.getQuantity()).isEqualTo(itemRequestDTO.getQuantity());
        assertThat(response.getLineItemKey().getOrderID()).isNotNull();
        assertThat(response.getLineItemKey().getProductID()).isEqualTo(itemRequestDTO.getProductID());

    }

    @Test
    public void test_quantity_not_sufficient_exception(){
        AppUser user = userFactory.createUser();

        Order order = orderFactory.createOrder(user);

        Product product = productFactory.createProduct();

        LineItemRequestDTO itemRequestDTO = new LineItemRequestDTO();
        itemRequestDTO.setOrderID(order.getId());
        itemRequestDTO.setProductID(product.getId());
        itemRequestDTO.setQuantity(product.getQuantity() + 100);

        assertThatThrownBy(() -> lineItemService.saveLineItem(user.getUsername() ,itemRequestDTO ))
                .isInstanceOf(QuantityNotSufficientException.class);
    }


    @Test
    public void test_line_item_already_exist_exception(){
        AppUser user = userFactory.createUser();

        Order order = orderFactory.createOrder(user);

        Product product = productFactory.createProduct();

        lineItemFactory.createLineItem(order, product , product.getQuantity() -5);

        LineItemRequestDTO itemRequestDTO = new LineItemRequestDTO();
        itemRequestDTO.setOrderID(order.getId());
        itemRequestDTO.setProductID(product.getId());
        itemRequestDTO.setQuantity(product.getQuantity() - 5);

        assertThatThrownBy(() -> lineItemService.saveLineItem(user.getUsername() ,itemRequestDTO))
                .isInstanceOf(ProductAlreadyInLineItemException.class);
    }


    @Test
    void test_remove_line_item_with_success() {
        AppUser user = userFactory.createUser();
        Order order = orderFactory.createOrder(user);
        Product product = productFactory.createProduct();

        lineItemFactory.createLineItem(order , product , product.getQuantity()-5);

        LineItemKey lineItemKey = new LineItemKey();
        lineItemKey.setOrderID(order.getId());
        lineItemKey.setProductID(product.getId());

        lineItemService.removeLineItem(user.getUsername() , lineItemKey);

        LineItem savedLineItem = lineItemRepository.findById(lineItemKey).orElse(null);

        assertThat(savedLineItem).isNull();
    }

    @Test
    void test_remove_line_item_by_user_not_who_create_order_exception() {
        AppUser user = userFactory.createUser();
        AppUser appUser = userFactory.createUser("omar");
        Order order = orderFactory.createOrder(user);
        Product product = productFactory.createProduct();

        lineItemFactory.createLineItem(order , product , product.getQuantity()-5);

        LineItemKey lineItemKey = new LineItemKey();
        lineItemKey.setOrderID(order.getId());
        lineItemKey.setProductID(product.getId());

        assertThatThrownBy(() -> lineItemService.removeLineItem(appUser.getUsername() , lineItemKey))
                .isInstanceOf(ForbiddenException.class);

    }

    @Test
    void test_change_quantity_order_with_success() {
        AppUser user = userFactory.createUser();
        Order order = orderFactory.createOrder(user);
        Product product = productFactory.createProduct();

        lineItemFactory.createLineItem(order , product , product.getQuantity()-5);

        int quantity = product.getQuantity() - 5;

        LineItemRequestDTO itemRequestDTO = new LineItemRequestDTO();
        itemRequestDTO.setOrderID(order.getId());
        itemRequestDTO.setProductID(product.getId());
        itemRequestDTO.setQuantity(quantity);

        LineItemResponseDTO response = lineItemService.changeQuantity(user.getUsername(), itemRequestDTO);

        assertThat(response.getLineItemKey().getProductID()).isEqualTo(product.getId());
        assertThat(response.getLineItemKey().getOrderID()).isEqualTo(order.getId());
        assertThat(response.getQuantity()).isEqualTo(quantity);
    }

    @Test
    void test_change_quantity_order_with_quantity_not_sufficient_exception() {
        AppUser user = userFactory.createUser();
        Order order = orderFactory.createOrder(user);
        Product product = productFactory.createProduct();

        lineItemFactory.createLineItem(order , product , product.getQuantity()-5);

        int quantity = product.getQuantity() + 5;

        LineItemRequestDTO itemRequestDTO = new LineItemRequestDTO();
        itemRequestDTO.setOrderID(order.getId());
        itemRequestDTO.setProductID(product.getId());
        itemRequestDTO.setQuantity(quantity);

        assertThatThrownBy(() -> lineItemService.changeQuantity(user.getUsername(), itemRequestDTO))
                .isInstanceOf(QuantityNotSufficientException.class);

    }
}