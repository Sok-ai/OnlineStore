package com.example.onlinestore;

import com.example.onlinestore.dao.OrderDAO;
import com.example.onlinestore.dao.OrderDAOImpl;
import com.example.onlinestore.dao.ProductDAO;
import com.example.onlinestore.dao.ProductDAOImpl;
import com.example.onlinestore.entity.Order;
import com.example.onlinestore.entity.Product;
import com.example.onlinestore.service.OrderService;
import com.example.onlinestore.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ProductDAO productDAO = new ProductDAOImpl(sessionFactory);
        OrderDAO orderDAO = new OrderDAOImpl(sessionFactory);
        OrderService orderService = new OrderService(orderDAO, productDAO);

        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while (choice != 5) {
            System.out.print("Выберите действие:\n" + "1 - Добавить продукт\n" + "2 - Создать заказ\n" + "3 - Вывести заказы по имени клиента\n" + "4 - Экспортировать заказы в JSON\n" + "5 – Выход\n");
            System.out.print("\nВаше число: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    scanner.nextLine();
                    System.out.print("Введите название продукта: ");
                    String productName = scanner.nextLine().trim();
                    if (productName.isEmpty()) {
                        System.out.println("Название не может быть пустым\n");
                        break;
                    }

                    BigDecimal productPrice = null;
                    while (productPrice == null) {
                        System.out.print("Введите цену продукта (формат: 100,50): ");
                        try {
                            productPrice = scanner.nextBigDecimal();
                            if (productPrice.compareTo(BigDecimal.ZERO) <= 0) {
                                System.out.println("Цена должна быть больше 0");
                                productPrice = null;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Ошибка! Используйте формат: 99,99");
                            scanner.nextLine();
                        }
                    }
                    Product product = new Product();
                    product.setName(productName);
                    product.setPrice(productPrice);
                    productDAO.addProduct(product);
                    System.out.println("Продукт добавлен.\n");
                    break;
                }
                case 2: {
                    scanner.nextLine();
                    System.out.print("Введите имя клиента: ");
                    String customerName = scanner.nextLine().trim();
                    if (customerName.isEmpty()) {
                        System.out.println("Имя клиента не может быть пустым\n");
                        break;
                    }

                    System.out.print("Введите список идентификаторов продуктов (через запятую c пробелом): ");
                    String productIdsInput = scanner.nextLine();

                    List<UUID> productIds;
                    try {
                        productIds = Arrays.stream(productIdsInput.split(",\\s*"))
                                .map(UUID::fromString)
                                .distinct()
                                .collect(Collectors.toList());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Ошибка: некорректный формат UUID\n");
                        break;
                    }

                    try {
                        orderService.createOrder(customerName, productIds);
                        System.out.println("Заказ создан.\n");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    } catch (RuntimeException e) {
                        System.out.println("Системная ошибка при создании заказа\n");
                        e.printStackTrace();
                    }
                    break;
                }
                case 3: {
                    scanner.nextLine();
                    System.out.print("Введите имя клиента: ");
                    String customerName = scanner.nextLine().trim();
                    List<Order> orderList = orderService.getOrderByName(customerName);
                    if (orderList.isEmpty()) {
                        System.out.println("Пока что нет заказов или неправильно указано имя\n");
                        break;
                    }
                    orderList.forEach(order -> System.out.println(order.toString()));
                    System.out.println();
                    break;
                }
                case 4: {
                    try {
                        orderService.setJsonFile();
                        System.out.println("JSON создан\n");
                        break;
                    } catch (IOException e) {
                        System.out.println("Еще нет заказов\n");
                        break;
                    }
                }
                case 5: {
                    System.out.println("До свидания!");
                    break;
                }
                default:
                    System.out.println("Неверный выбор.\n");
            }
        }
        sessionFactory.close();
    }
}
