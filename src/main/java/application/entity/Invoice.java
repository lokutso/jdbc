package application.entity;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;

public final class Invoice {
    private int id;
    private @NotNull Date date;
    private int organizationId;


    public Invoice(int id, @NotNull Date date, int organizationId) {
        this.id = id;
        this.date = date;
        this.organizationId = organizationId;
    }

    public int getId() {
        return this.id;
    }

    public @NotNull Date getDate() {
        return this.date;
    }

    public int getOrganizationId() {
        return this.organizationId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(@NotNull Date date) {
        this.date = date;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", date=" + date +
                ", organizationId=" + organizationId +
                '}';
    }
}
