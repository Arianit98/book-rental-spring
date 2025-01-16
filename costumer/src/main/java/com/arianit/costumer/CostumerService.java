package com.arianit.costumer;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CostumerService {

    private CostumerRepository costumerRepository;

    public CostumerService(CostumerRepository costumerRepository) {
        this.costumerRepository = costumerRepository;
    }

    public Costumer createCostumer(Costumer costumer) {
        return costumerRepository.save(costumer);
    }

    public Costumer getCostumer(Long id) {
        return costumerRepository.findById(id).orElseThrow();
    }

    public void deleteCostumer(Long id) {
        costumerRepository.deleteById(id);
    }

    public Costumer updateCostumer(Long id, Costumer newCostumer) {
        return costumerRepository.findById(id)
                .map(costumer -> {
                    costumer.setName(newCostumer.getName());
                    costumer.setEmail(newCostumer.getEmail());
                    costumer.setPhone(newCostumer.getPhone());
                    costumer.setAddress(newCostumer.getAddress());
                    costumer.setAge(newCostumer.getAge());
                    return costumerRepository.save(costumer);
                })
                .orElseGet(() -> costumerRepository.save(newCostumer));
    }

    public List<Costumer> getCostumers() {
        return costumerRepository.findAll();
    }
}
