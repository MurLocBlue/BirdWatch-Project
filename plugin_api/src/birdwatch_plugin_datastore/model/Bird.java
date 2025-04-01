package birdwatch_plugin_datastore.model;

import java.time.LocalDateTime;

/**
 * Model class representing a bird in the BirdWatch system.
 * Contains information about the bird's physical characteristics.
 *
 * @author Costin Marinescu
 * @version 0.1
 */
public class Bird {
    private Long id;
	private String name;
    private String color;
    private Double weight;
    private Double height;
    private LocalDateTime createdAt;
    
    public Bird() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id The unique identifier of the bird
     * @param name The name of the bird
     * @param color The color of the bird
     * @param weight The weight of the bird in kilograms
     * @param height The height of the bird in centimeters
     * @param createdAt The timestamp when the bird was created
     */
    public Bird(Long id, String name, String color, Double weight, Double height, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.weight = weight;
        this.height = height;
        this.createdAt = createdAt;
    }

    /**
     * Gets the unique identifier of the bird.
     *
     * @return The bird's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the bird.
     *
     * @param id The bird's ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the bird.
     *
     * @return The bird's name
     */
    public String getName() {
		return name;
	}

    /**
     * Sets the name of the bird.
     *
     * @param name The bird's name
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Gets the color of the bird.
     *
     * @return The bird's color
     */
	public String getColor() {
		return color;
	}

    /**
     * Sets the color of the bird.
     *
     * @param color The bird's color
     */
	public void setColor(String color) {
		this.color = color;
	}

    /**
     * Gets the weight of the bird.
     *
     * @return The bird's weight in kilograms
     */
	public Double getWeight() {
		return weight;
	}

    /**
     * Sets the weight of the bird.
     *
     * @param weight The bird's weight in kilograms
     */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

    /**
     * Gets the height of the bird.
     *
     * @return The bird's height in centimeters
     */
	public Double getHeight() {
		return height;
	}

    /**
     * Sets the height of the bird.
     *
     * @param height The bird's height in centimeters
     */
	public void setHeight(Double height) {
		this.height = height;
	}

    /**
     * Gets the creation timestamp of the bird.
     *
     * @return The timestamp when the bird was created
     */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

    /**
     * Sets the creation timestamp of the bird.
     *
     * @param createdAt The timestamp when the bird was created
     */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
} 