package com.example.lujuria.commerce.service;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.catalog.entity.Category;
import com.example.lujuria.catalog.entity.CategoryType;
import com.example.lujuria.catalog.repository.CategoryRepository;
import com.example.lujuria.common.BusinessRuleException;
import com.example.lujuria.common.ResourceNotFoundException;
import com.example.lujuria.commerce.dto.CreateOrderRequest;
import com.example.lujuria.commerce.dto.OrderLineRequest;
import com.example.lujuria.commerce.dto.OrderResponse;
import com.example.lujuria.commerce.dto.ProductRequest;
import com.example.lujuria.commerce.dto.ProductResponse;
import com.example.lujuria.commerce.entity.CustomerOrder;
import com.example.lujuria.commerce.entity.OrderItem;
import com.example.lujuria.commerce.entity.OrderStatus;
import com.example.lujuria.commerce.entity.PaymentStatus;
import com.example.lujuria.commerce.entity.PaymentTransaction;
import com.example.lujuria.commerce.entity.Product;
import com.example.lujuria.commerce.entity.ProductType;
import com.example.lujuria.commerce.repository.CustomerOrderRepository;
import com.example.lujuria.commerce.repository.PaymentTransactionRepository;
import com.example.lujuria.commerce.repository.ProductRepository;
import com.example.lujuria.creator.entity.CreatorProfile;
import com.example.lujuria.creator.service.CreatorService;
import com.example.lujuria.user.entity.AppUser;
import com.example.lujuria.user.service.UserService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommerceService {

    private final ProductRepository productRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final CategoryRepository categoryRepository;
    private final CreatorService creatorService;
    private final UserService userService;

    public CommerceService(
        ProductRepository productRepository,
        CustomerOrderRepository customerOrderRepository,
        PaymentTransactionRepository paymentTransactionRepository,
        CategoryRepository categoryRepository,
        CreatorService creatorService,
        UserService userService
    ) {
        this.productRepository = productRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.categoryRepository = categoryRepository;
        this.creatorService = creatorService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> listProducts(String category, ProductType type) {
        Specification<Product> specification = Specification.allOf(
            ProductSpecifications.isActive(),
            ProductSpecifications.hasCategorySlug(category),
            ProductSpecifications.hasType(type)
        );

        return productRepository.findAll(specification).stream()
            .map(ProductResponse::from)
            .toList();
    }

    @Transactional
    public ProductResponse createProduct(AppUserPrincipal principal, ProductRequest request) {
        CreatorProfile creator = creatorService.getRequiredCreatorByUserId(principal.getUserId());
        Category category = categoryRepository.findBySlug(request.categorySlug().trim().toLowerCase(Locale.ROOT))
            .filter(existing -> existing.getType() == CategoryType.PRODUCT)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria de productos no encontrada."));

        Product product = new Product();
        product.setCreator(creator);
        product.setCategory(category);
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setType(request.type());
        product.setPrice(request.price());
        product.setInventory(request.inventory());
        product.setImageUrl(request.imageUrl());

        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public OrderResponse createOrder(AppUserPrincipal principal, CreateOrderRequest request) {
        AppUser buyer = userService.getRequiredUser(principal.getUserId());
        Map<Long, Product> productMap = productRepository.findAllById(
            request.items().stream().map(OrderLineRequest::productId).toList()
        ).stream().collect(Collectors.toMap(Product::getId, product -> product));

        CustomerOrder order = new CustomerOrder();
        order.setBuyer(buyer);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderLineRequest line : request.items()) {
            Product product = productMap.get(line.productId());
            if (product == null) {
                throw new ResourceNotFoundException("Uno de los productos no existe.");
            }
            if (!product.isActive()) {
                throw new BusinessRuleException("Uno de los productos ya no esta disponible.");
            }
            if (product.getInventory() < line.quantity()) {
                throw new BusinessRuleException("Inventario insuficiente para el producto " + product.getName() + ".");
            }

            product.setInventory(product.getInventory() - line.quantity());

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setProductName(product.getName());
            item.setQuantity(line.quantity());
            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
            order.addItem(item);

            totalAmount = totalAmount.add(item.getSubtotal());
        }

        order.setTotalAmount(totalAmount);
        CustomerOrder savedOrder = customerOrderRepository.save(order);

        PaymentTransaction payment = new PaymentTransaction();
        payment.setCustomerOrder(savedOrder);
        payment.setAmount(savedOrder.getTotalAmount());
        payment.setStatus(PaymentStatus.APPROVED);
        payment.setExternalReference("SIM-" + savedOrder.getId() + "-" + Instant.now().toEpochMilli());
        payment.setPaidAt(Instant.now());
        paymentTransactionRepository.save(payment);

        savedOrder.setPayment(payment);
        savedOrder.setStatus(OrderStatus.PAID);
        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> myOrders(AppUserPrincipal principal) {
        return customerOrderRepository.findByBuyerIdOrderByCreatedAtDesc(principal.getUserId()).stream()
            .map(OrderResponse::from)
            .toList();
    }
}
