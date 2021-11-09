package application.entity;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public final class Organization {
    private int id;
    private @NotNull String name;
    private @NotNull BigInteger inn;
    private @NotNull BigInteger payment_account;

    public Organization(int id, @NotNull String name, @NotNull BigInteger inn, @NotNull BigInteger payment_account) {
        this.id = id;
        this.name = name;
        this.inn = inn;
        this.payment_account = payment_account;
    }

    public int getId() {
        return this.id;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull BigInteger getInn() {
        return this.inn;
    }

    public @NotNull BigInteger getPayment_account() {
        return this.payment_account;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setInn(@NotNull BigInteger inn) {
        this.inn = inn;
    }

    public void setPayment_account(@NotNull BigInteger payment_account) {
        this.payment_account = payment_account;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", inn=" + inn +
                ", payment_account=" + payment_account +
                '}';
    }
}
