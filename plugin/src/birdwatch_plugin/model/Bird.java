package birdwatch_plugin.model;

import java.time.LocalDateTime;

public class Bird {
    private Long id;
	private String name;
    private String color;
    private Double weight;
    private Double height;
    private LocalDateTime createdAt;
    
    public Bird() {
    }

    public Bird(Long id, String name, String color, Double weight, Double height, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.weight = weight;
        this.height = height;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    

} 