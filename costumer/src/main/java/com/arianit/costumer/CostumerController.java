package com.arianit.costumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/costumers")
public class CostumerController {

    CostumerService costumerService;

    public CostumerController(CostumerService costumerService) {
        this.costumerService = costumerService;
    }

    @GetMapping
    public List<Costumer> getCostumers() {
        return costumerService.getCostumers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Costumer> getCostumer(@PathVariable("id") Long id) {
        Costumer costumer = costumerService.getCostumer(id);
        if (costumer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(costumer);
    }

    @PostMapping
    public ResponseEntity<Costumer> createCostumer(@RequestBody Costumer newCostumer) {
        Costumer costumer = costumerService.createCostumer(newCostumer);
        return ResponseEntity.status(HttpStatus.CREATED).body(costumer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Costumer> updateCostumer(@PathVariable("id") Long id, @RequestBody Costumer newCostumer) {
        Costumer costumer = costumerService.updateCostumer(id, newCostumer);
        return ResponseEntity.status(HttpStatus.CREATED).body(costumer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCostumer(@PathVariable("id") Long id) {
        costumerService.deleteCostumer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
