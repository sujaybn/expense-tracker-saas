package com.exptracker.expense_tracker_api.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "categories",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "tenant_id"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "tenant_id", nullable = false)
//    private Tenant tenant;
    
    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Expense> expenses;

}
