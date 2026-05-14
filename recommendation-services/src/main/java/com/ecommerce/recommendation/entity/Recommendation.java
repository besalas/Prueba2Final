package com.ecommerce.recommendation.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Table(name = "recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String customerEmail;

    @Column(nullable = false)
    private Long recommendedProductId;

    @Column(nullable = false, length = 255)
    private String personalizedMessage; // Ej: "Porque compraste una placa madre, te sugerimos esta RAM, este procesador y gpu "

    @Column(nullable = false)
    private LocalDateTime generatedAt;
}
