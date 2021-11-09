package application.entity;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Objects;


public final class Product {
    private int id;
    private @NotNull String name;
    private @NotNull BigInteger internalCode;

    public Product(int id, @NotNull String name, @NotNull BigInteger internal_code) {
        this.id = id;
        this.name = name;
        this.internalCode = internal_code;
    }

    public int getId() {
        return this.id;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull BigInteger getInternalCode() {
        return this.internalCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setInternalCode(@NotNull BigInteger internalCode) {
        this.internalCode = internalCode;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", internal_code=" + internalCode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && name.equals(product.name) && internalCode.equals(product.internalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, internalCode);
    }
}
