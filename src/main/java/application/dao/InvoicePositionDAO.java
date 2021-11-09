package application.dao;

import application.entity.Invoice;
import application.entity.InvoicePosition;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class InvoicePositionDAO implements DAO<InvoicePosition> {
    private final @NotNull Connection connection;

    public InvoicePositionDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public @NotNull InvoicePosition get(int id) {
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT id, invoice_id, price, product_id, count FROM invoice_position WHERE id = " + id)) {
                if (resultSet.next()) {
                    return new InvoicePosition(resultSet.getInt("id"), resultSet.getInt("invoice_id"), resultSet.getBigDecimal("price"), resultSet.getInt("product_id"), resultSet.getInt("count"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalStateException("Record with id " + id + "not found");
    }

    @Override
    public @NotNull List<InvoicePosition> all() {
        final List<InvoicePosition> result = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT id, invoice_id, price, product_id, count FROM invoice_position")) {
                while (resultSet.next()) {
                    result.add(new InvoicePosition(resultSet.getInt("id"), resultSet.getInt("invoice_id"), resultSet.getBigDecimal("price"), resultSet.getInt("product_id"), resultSet.getInt("count")));
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public void save(@NotNull InvoicePosition entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO invoice_position(id, invoice_id, price, product_id, count) VALUES(?,?,?,?,?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setInt(2, entity.getInvoiceId());
            preparedStatement.setBigDecimal(3, entity.getPrice());
            preparedStatement.setInt(4, entity.getProductId());
            preparedStatement.setInt(5, entity.getCount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(@NotNull InvoicePosition entity) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE invoice_position SET invoice_id = ?, price = ?, product_id = ?, count = ? WHERE id = ?")) {
            int fieldIndex = 1;
            preparedStatement.setInt(fieldIndex++, entity.getInvoiceId());
            preparedStatement.setBigDecimal(fieldIndex++, entity.getPrice());
            preparedStatement.setInt(fieldIndex++, entity.getProductId());
            preparedStatement.setInt(fieldIndex++, entity.getCount());
            preparedStatement.setInt(fieldIndex++, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(@NotNull InvoicePosition entity) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM invoice_position WHERE id = ?")) {
            preparedStatement.setInt(1, entity.getId());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
