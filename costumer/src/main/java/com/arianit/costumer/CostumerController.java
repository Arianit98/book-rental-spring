package com.arianit.costumer;

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
    public Costumer getCostumer(@PathVariable Long id) {
        return costumerService.getCostumer(id);
    }

    @PostMapping
    public Costumer createCostumer(@RequestBody Costumer request) {
        return costumerService.createCostumer(request);
    }

    @PutMapping("/{id}")
    public Costumer updateCostumer(@PathVariable Long id, @RequestBody Costumer newCostumer) {
        return costumerService.updateCostumer(id, newCostumer);
    }

    @DeleteMapping("/{id}")
    public void deleteCostumer(@PathVariable Long id) {
        costumerService.deleteCostumer(id);
    }


}
