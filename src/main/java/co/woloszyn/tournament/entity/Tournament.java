package co.woloszyn.tournament.entity;

import co.woloszyn.customer.entity.Customer;
import co.woloszyn.tournament.enumeration.TournamentStatus;
import co.woloszyn.tournament.enumeration.TournamentType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Tournament extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "online")
    private Boolean online;

    @Column(unique = true, nullable = false)
    private String description;

    @Column(name = "date")
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private TournamentType type;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "place", nullable = false)
    public Place place;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "tournament")
    private List<TournamentAdditionalInfo> additionalInfo;

    @OneToMany(mappedBy = "tournament")
    private List<TournamentSubscription> subscriptions;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "max_quantity")
    private Integer maxQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TournamentStatus status;

    public Long getId() { return id; }

    public Place getPlace() { return place; }
    public void setPlace(Place place) { this.place = place; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public TournamentType getType() { return type; }
    public void setType(TournamentType type) { this.type = type; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TournamentAdditionalInfo> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(List<TournamentAdditionalInfo> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<TournamentSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<TournamentSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }
}
