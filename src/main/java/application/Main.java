package application;

import application.commons.FlywayInitializer;
import application.commons.JDBCCredentials;
import application.dao.InvoiceDAO;
import application.dao.InvoicePositionDAO;
import application.dao.OrganizationDAO;
import application.dao.ProductDAO;
import application.entity.Invoice;
import application.entity.InvoicePosition;
import application.entity.Organization;
import application.entity.Product;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public final class Main {

    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    public static void main(String[] args) {
        FlywayInitializer.initDb();
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            final var productDAO = new ProductDAO(connection);
            for (int i = 0; i < 20; i++) {
                productDAO.save(new Product(i, "Product " + i, new BigInteger(String.valueOf(i + 100000))));
            }
            productDAO.all().forEach(System.out::println);

            final var organizationDAO = new OrganizationDAO(connection);
            for (int i = 0; i < 20; i++) {
                organizationDAO.save(
                        new Organization(
                                i,
                                "Organization " + i,
                                new BigInteger(String.valueOf(i + 10000)),
                                new BigInteger(String.valueOf(i + 90000))));
            }
            organizationDAO.all().forEach(System.out::println);

            final var invoiceDAO = new InvoiceDAO(connection);
            for (int i = 0; i < 20; i++) {
                invoiceDAO.save(
                        new Invoice(
                                i,
                                new Date(new GregorianCalendar(2021, i, 1).getTimeInMillis()),
                                organizationDAO.get(i).getId()));
            }
            invoiceDAO.all().forEach(System.out::println);

            final var invoicePositionDAO = new InvoicePositionDAO(connection);
            for (int i = 0; i < 20; i++) {
                invoicePositionDAO.save(
                        new InvoicePosition(
                                i,
                                invoiceDAO.get(i).getId(),
                                new BigDecimal(String.valueOf(10000)),
                                productDAO.get(i).getId(), (i + 1) * 100));
            }
            invoicePositionDAO.all().forEach(System.out::println);


            System.out.println(organizationDAO.selectFirstTenSuppliersAccordingToQuantityOfDeliveredProducts());
            System.out.println(organizationDAO.selectSuppliersWithAmountOfDeliveredProductsAboveSpecifiedQuantity(400));
            Map map;
            try {
                map = forEachDayForEachProductCalculateQuantityAndAmountOfReceivedProductInSpecifiedPeriod(
                        new Date(new GregorianCalendar(2021, 1, 1).getTimeInMillis()),
                        new Date(new GregorianCalendar(2021, 3, 1).getTimeInMillis()),
                        invoiceDAO, invoicePositionDAO, productDAO);
                System.out.println(map);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
    }

    public static List<Organization> selectSuppliersWithAmountOfDeliveredProductsAboveSpecifiedQuantity(
            int amount, InvoicePositionDAO invoicePositionDAO, InvoiceDAO invoiceDAO, OrganizationDAO organizationDAO) {
        List<Integer> invoicePositionList = invoicePositionDAO.all().stream()
                .filter(invoicePosition -> invoicePosition.getCount() > amount)
                .map(InvoicePosition::getInvoiceId)
                .collect(Collectors.toList());
        List<Integer> invoiceList = invoiceDAO.all().stream()
                .filter(invoice -> invoicePositionList.contains(invoice.getId()))
                .map(Invoice::getOrganizationId)
                .collect(Collectors.toList());
        return organizationDAO.all().stream()
                .filter(organization -> invoiceList.contains(organization.getId()))
                .collect(Collectors.toList());
    }

    public static Map forEachDayForEachProductCalculateQuantityAndAmountOfReceivedProductInSpecifiedPeriod(
            Date date1, Date date2, InvoiceDAO invoiceDAO, InvoicePositionDAO invoicePositionDAO, ProductDAO productDAO) throws Exception {
        class Result {
            int count;
            @NotNull BigDecimal sum;
            Result() {
                this.count = 0;
                this.sum = new BigDecimal(0);
            }
        }
        HashMap<Date, HashMap<Product, Result>> result = new HashMap<>();
        List<Product> productList = productDAO.all();
        while (date1.getTime() <= date2.getTime()) {
            List<Integer> invoiceList = invoiceDAO.all().stream()
                    .filter(invoice -> invoice.getDate().getTime() >= date1.getTime() &&
                            invoice.getDate().getTime() < (date1.getTime() + 1000*60*60*24))
                    .map(Invoice::getId).collect(Collectors.toList());
            List<InvoicePosition> invoicePositionList = invoicePositionDAO.all().stream()
                    .filter(invoicePosition -> invoiceList.contains(invoicePosition.getInvoiceId()))
                    .collect(Collectors.toList());
            Set<Integer> products = invoicePositionList.stream()
                    .map(InvoicePosition::getProductId)
                    .collect(Collectors.toSet());
            for (Integer s : products) {
                List<InvoicePosition> invoicePositionList1 = invoicePositionList.stream()
                        .filter(invoicePosition -> s.equals(invoicePosition.getProductId()))
                        .collect(Collectors.toList());
                Result result1 = new Result();
                invoicePositionList1.stream().forEach(invoicePosition -> {
                    result1.count += invoicePosition.getCount();
                    result1.sum = new BigDecimal(String.valueOf(invoicePosition.getPrice()));
                });
                Optional<Product> currentProduct = productList.stream().filter(product -> product.getId() == s).findFirst();
                if (currentProduct.isPresent()) {
                    Product product = currentProduct.get();
                    HashMap<Product, Result> map = new HashMap<>();
                    map.put(product, result1);
                    result.put(new Date(date1.getTime()), map);
                } else {
                    throw new Exception("Not found product");
                }
            }
            date1.setTime(date1.getTime() + 1000*60*60*24);
        }
        return result;
    }
}
