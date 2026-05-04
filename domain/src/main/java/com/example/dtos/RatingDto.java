package com.example.dtos;

public class RatingDto {

    private double averageRate;
    private long ratingCount;
    private Integer userRating;

    public RatingDto() {}

    public RatingDto(double averageRate, long ratingCount, Integer userRating) {
        this.averageRate = averageRate;
        this.ratingCount = ratingCount;
        this.userRating = userRating;
    }

    public double getAverageRate() { return averageRate; }
    public void setAverageRate(double averageRate) { this.averageRate = averageRate; }

    public long getRatingCount() { return ratingCount; }
    public void setRatingCount(long ratingCount) { this.ratingCount = ratingCount; }

    public Integer getUserRating() { return userRating; }
    public void setUserRating(Integer userRating) { this.userRating = userRating; }
}
