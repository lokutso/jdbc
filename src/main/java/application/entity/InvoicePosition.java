package application.entity;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public final class InvoicePosition {
    private int id;
    private int invoiceId;
    private @NotNull BigDecimal price;
    private int productId;
    private int count;

    public InvoicePosition(int id, int invoiceId, @NotNull BigDecimal price, int productId, int count) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.price = price;
        this.productId = productId;
        this.count = count;
    }

    public int getId() {
        return this.id;
    }

    public int getInvoiceId() {
        return this.invoiceId;
    }

    public @NotNull BigDecimal getPrice() {
        return this.price;
    }

    public int getProductId() {
        return this.productId;
    }

    public int getCount() {
        return this.count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setPrice(@NotNull BigDecimal price) {
        this.price = price;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "InvoicePosition{" +
                "id=" + id +
                ", invoiceId=" + invoiceId +
                ", price=" + price +
                ", productId=" + productId +
                ", count=" + count +
                '}';
    }
}
