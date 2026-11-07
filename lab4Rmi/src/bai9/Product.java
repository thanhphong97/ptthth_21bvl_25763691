package bai9;

import java.io.Serializable;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String seller;
    private final double startPrice;
    private double currentPrice;
    private String highestBidder;

    public Product(String name, String seller, double startPrice) {
        this.name = name;
        this.seller = seller;
        this.startPrice = startPrice;
        this.currentPrice = startPrice;
    }

    public String getName() {
        return name;
    }

    public String getSeller() {
        return seller;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    public void setBid(String bidder, double price) {
        this.highestBidder = bidder;
        this.currentPrice = price;
    }

    @Override
    public String toString() {
        return name + " | Người bán: " + seller
                + " | Giá khởi điểm: " + startPrice
                + " | Giá hiện tại: " + currentPrice
                + " | Người trả cao nhất: " + (highestBidder == null ? "chưa có" : highestBidder);
    }
}
