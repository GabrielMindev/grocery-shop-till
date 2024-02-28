package com.example.groceryshoptill;

import com.example.groceryshoptill.service.GroceryShopService;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroceryShopServiceTest {
    GroceryShopService groceryShopService = new GroceryShopService();
    @Test
    void addItem() {
        groceryShopService.addItem("Apple", 100);
        assertNotNull(groceryShopService.getItems().get("Apple"));
        assertEquals(100, groceryShopService.getItems().get("Apple").getPrice());
    }

    @Test
    void testDeleteItem() {
        groceryShopService.addItem("Banana", 150);
        assertTrue(groceryShopService.getItems().containsKey("Banana"));
        groceryShopService.deleteItem("Banana");
        assertFalse(groceryShopService.getItems().containsKey("Banana"));
    }

    @Test
    void testAddDeal() {
        groceryShopService.addItem("Apple", 50);
        groceryShopService.addItem("Banana", 40);

        List<String> dealItems = Arrays.asList("Apple", "Banana");
        groceryShopService.addDeal(GroceryShopService.DEAL_TYPE_2_FOR_3, dealItems);
        assertNotNull(groceryShopService.getDeals().get(GroceryShopService.DEAL_TYPE_2_FOR_3));
        assertEquals(dealItems, groceryShopService.getDeals().get(GroceryShopService.DEAL_TYPE_2_FOR_3).getItems());
    }

    @Test
    void testDeleteDeal() {
        List<String> dealItems = Arrays.asList("Apple", "Banana");
        groceryShopService.addItem("Apple", 50);
        groceryShopService.addItem("Banana", 40);

        groceryShopService.addDeal(GroceryShopService.DEAL_TYPE_2_FOR_3, dealItems);
        assertTrue(groceryShopService.getDeals().containsKey(GroceryShopService.DEAL_TYPE_2_FOR_3));
        groceryShopService.deleteDeal(GroceryShopService.DEAL_TYPE_2_FOR_3);
        assertFalse(groceryShopService.getDeals().containsKey(GroceryShopService.DEAL_TYPE_2_FOR_3));
    }

    @Test
    void testScanItems() {
        groceryShopService.addItem("Apple", 50);
        groceryShopService.addItem("Banana", 40);
        groceryShopService.addItem("Tomato",30);
        groceryShopService.addItem("Potato",26);
        List<String>dealItems1= List.of("Potato");
        List<String>dealItems2=List.of("Apple","Banana","Tomato");

        List<String> scannedItems = Arrays.asList("Apple", "Banana", "Banana","Potato","Tomato","Banana","Potato");
        groceryShopService.addDeal(GroceryShopService.DEAL_TYPE_BUY_1_GET_1_HALF_PRICE, dealItems1);
        groceryShopService.addDeal(GroceryShopService.DEAL_TYPE_2_FOR_3, dealItems2);

        double totalCost = groceryShopService.scanItems(scannedItems);
        assertEquals(1.99, totalCost);
    }
}