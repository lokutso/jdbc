package application.dao;

import application.entity.InvoicePosition;
import application.entity.Organization;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class OrganizationDAO implements DAO<Organization> {
    private final @NotNull Connection connection;

    public OrganizationDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public @NotNull Organization get(int id) {
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT id, name, inn, payment_account FROM organization WHERE id = " + id)) {
                if (resultSet.next()) {
                    return new Organization(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getBigDecimal("inn").toBigInteger(), resultSet.getBigDecimal("payment_account").toBigInteger());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalStateException("Record with id " + id + "not found");
    }

    @Override
    public @NotNull List<Organization> all() {
        final List<Organization> result = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT id, name, inn, payment_account FROM organization")) {
                while (resultSet.next()) {
                    result.add(new Organization(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getBigDecimal("inn").toBigInteger(), resultSet.getBigDecimal("payment_account").toBigInteger()));
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public void save(@NotNull Organization entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO organization(id, name, inn, payment_account) VALUES(?,?,?,?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setBigDecimal(3, new BigDecimal(entity.getInn()));
            preparedStatement.setBigDecimal(4, new BigDecimal(entity.getPayment_account()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(@NotNull Organization entity) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE organization SET name = ?, inn = ?, payment_account = ? WHERE id = ?")) {
            int fieldIndex = 1;
            preparedStatement.setString(fieldIndex++, entity.getName());
            preparedStatement.setBigDecimal(fieldIndex++, new BigDecimal(entity.getInn()));
            preparedStatement.setBigDecimal(fieldIndex++, new BigDecimal(entity.getPayment_account()));
            preparedStatement.setInt(fieldIndex++, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(@NotNull Organization entity) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM organization WHERE id = ?")) {
            preparedStatement.setInt(1, entity.getId());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public @NotNull List<String> selectFirstTenSuppliersAccordingToQuantityOfDeliveredProducts() {
        final List<String> result = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("" +
                    "SELECT organization.name FROM organization " +
                    "INNER JOIN invoice ON organization.id = invoice.organization_id " +
                    "INNER JOIN invoice_position ON invoice.id = invoice_position.invoice_id " +
                    "ORDER BY invoice_position.count DESC " +
                    "LIMIT 10")) {
                while (resultSet.next()) {
                    result.add(new String(resultSet.getString("name")));
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public @NotNull List<String> selectSuppliersWithAmountOfDeliveredProductsAboveSpecifiedQuantity(int amount) {
        final List<String> result = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("" +
                    "SELECT organization.name FROM organization " +
                    "INNER JOIN invoice ON organization.id = invoice.organization_id " +
                    "INNER JOIN invoice_position ON invoice.id = invoice_position.invoice_id " +
                    "WHERE invoice_position.count > " + amount + " ")) {
                while (resultSet.next()) {
                    result.add(new String(resultSet.getString("name")));
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
