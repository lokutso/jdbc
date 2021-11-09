package application.dao;

import application.entity.Product;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ProductDAO implements DAO<Product> {
    private final @NotNull Connection connection;

    public ProductDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public @NotNull Product get(int id) {
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT id, name, internal_code FROM product WHERE id = " + id)) {
                if (resultSet.next()) {
                    return new Product(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getBigDecimal("internal_code").toBigInteger());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalStateException("Record with id " + id + "not found");
    }

    @Override
    public @NotNull List<Product> all() {
        final List<Product> result = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT id, name, internal_code FROM product")) {
                while (resultSet.next()) {
                    result.add(new Product(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getBigDecimal("internal_code").toBigInteger()));
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public void save(@NotNull Product entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO product(id, name, internal_code) VALUES(?,?,?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setBigDecimal(3, new BigDecimal(entity.getInternalCode()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(@NotNull Product entity) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE product SET name = ?, internal_code = ? WHERE id = ?")) {
            int fieldIndex = 1;
            preparedStatement.setString(fieldIndex++, entity.getName());
            preparedStatement.setBigDecimal(fieldIndex++, new BigDecimal(entity.getInternalCode()));
            preparedStatement.setInt(fieldIndex++, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(@NotNull Product entity) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM product WHERE id = ?")) {
            preparedStatement.setInt(1, entity.getId());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
