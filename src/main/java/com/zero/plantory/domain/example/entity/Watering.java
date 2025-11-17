package com.zero.plantory.domain.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "watering")
public class Watering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wateringId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "myplant_id")
    private MyPlant myPlant;

    private String interval;
    private LocalDateTime term;
    private LocalDateTime createdAt;

    private boolean checked;
}

