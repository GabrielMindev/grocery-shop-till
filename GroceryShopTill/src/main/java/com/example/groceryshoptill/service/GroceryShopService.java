package com.example.groceryshoptill.service;

import com.example.groceryshoptill.exceptions.NotFoundException;
import com.example.groceryshoptill.model.GroceryItem;
import com.example.groceryshoptill.model.SpecialDeal;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Service
public class GroceryShopService {
    public static final String DEAL_TYPE_2_FOR_3 = "2 for 3";
    public static final String DEAL_TYPE_BUY_1_GET_1_HALF_PRICE = "buy 1 get 1 half price";
    private Map<String, GroceryItem> items = new HashMap<>();
    private Map<String, SpecialDeal> deals = new HashMap<>();

    public void addItem(String name, int price) {
        items.put(name, new GroceryItem(name, price));
    }

    public void deleteItem(String name) {
        if (!items.containsKey(name)) {
            throw new NotFoundException("Item not found");
        }
        items.remove(name);
    }

    public void addDeal(String type, List<String> items) {
        for (String item : items) {
            if (!this.items.containsKey(item)) {
                throw new NotFoundException("Item not found: " + item);
            }
        }
        deals.put(type, new SpecialDeal(type, items));
    }


    public void deleteDeal(String type) {
        if (!deals.containsKey(type)) {
            throw new NotFoundException("Deal not found: " + type);
        }
        deals.remove(type);
    }

    //Scans a list of items, calculates the total cost, and applies special deals.
    public double scanItems(List<String> scannedItems) {
        double totalCost = 0;

        // Map to store the count of each item
        Map<String, Integer> itemCounts = new HashMap<>();

        // Iterate through scanned items
        for (String item : scannedItems) {
            // Check if the item exists in the items map
            if (items.containsKey(item)) {
                totalCost += items.get(item).getPrice();
                // Update the count of the scanned item
                itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
            } else {
                // If item not found, throw an exception
                throw new NotFoundException("Item not found");
            }
        }

        // Apply special deals
        for (SpecialDeal deal : deals.values()) {
            if (deal.getType().equals(DEAL_TYPE_2_FOR_3)) {
                totalCost = apply2For3Deal(totalCost, deal.getItems(), scannedItems);
            } else if (deal.getType().equals(DEAL_TYPE_BUY_1_GET_1_HALF_PRICE)) {
                totalCost = applyBuy1Get1HalfPriceDeal(totalCost, itemCounts, deal.getItems());
            }
        }

        return totalCost / 100.0; // Convert back to aws
    }

    //The deal reduce the total cost based on specific conditions
    private double apply2For3Deal(double totalCost, List<String> dealItems, List<String> scannedItems) {
        // Count of items eligible for the deal
        int count = 0;
        double minPrice = Integer.MAX_VALUE;

        // Iterate through scanned items
        for (String item : scannedItems) {
            // Check if the item is eligible for the deal and the count is less than 3
            if (dealItems.contains(item) && count < 3) {
                count++;
                double itemPrice = items.get(item).getPrice();
                // Update the minimum price if the current item price is lower
                if (itemPrice < minPrice) {
                    minPrice = itemPrice;
                }
            }
        }
        // Subtract the minimum price from the total cost
        totalCost -= minPrice;
        return totalCost;
    }

    //The deal reduce the total cost based on specific conditions
    private double applyBuy1Get1HalfPriceDeal(double totalCost, Map<String, Integer> itemCounts, List<String> dealItems) {
        // Iterate through items eligible for the deal
        for (String item : dealItems) {
            //Get the count of the item
            int count = itemCounts.getOrDefault(item, 0);
            //Estimate the number of discounted items
            int discountedItems = count / 2;
            //Reduce the discounted amount from the total price
            totalCost -= discountedItems * items.get(item).getPrice() / 2.0;
        }
        return totalCost;
    }
}