package com.example.groceryshoptill.controller;

import com.example.groceryshoptill.model.GroceryItem;
import com.example.groceryshoptill.service.GroceryShopService;
import com.example.groceryshoptill.model.SpecialDeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/grocery")
@CrossOrigin(origins = "http://localhost:8080")
public class GroceryController {

    @Autowired
    private GroceryShopService groceryShopService;

    @GetMapping("/items")
    public Map<String, GroceryItem> getItems() {
        return groceryShopService.getItems();
    }

    @GetMapping("/deals")
    public Map<String, SpecialDeal> getDeals() {
        return groceryShopService.getDeals();
    }

    @PostMapping("/items")
    public ResponseEntity<String> addItem(@RequestBody GroceryItem groceryItem) {
        groceryShopService.addItem(groceryItem.getName(), groceryItem.getPrice());
        String responseMessage = "Item added successfully";
        return ResponseEntity.ok(responseMessage);
    }

    @DeleteMapping("/items/{name}")
    public void deleteItem(@PathVariable String name) {
        groceryShopService.deleteItem(name);
    }

    @PostMapping("/deals")
    public ResponseEntity<String> addDeal(@RequestBody SpecialDeal specialDeal) {
        groceryShopService.addDeal(specialDeal.getType(), specialDeal.getItems());
        String responseMessage = "Deal added successfully";
        return ResponseEntity.ok(responseMessage);
    }

    @DeleteMapping("/deals/{type}")
    public void deleteDeal(@PathVariable String type) {
        groceryShopService.deleteDeal(type);
    }

    @PostMapping("/scan")
    public ResponseEntity<Double> scanItems(@RequestBody List<String> scannedItems) {
        double totalCost = groceryShopService.scanItems(scannedItems);
        return ResponseEntity.ok(totalCost);
    }
}
