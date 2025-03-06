package com.arianit.costumer;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CostumerService {

    private final CostumerRepository costumerRepository;

    public CostumerService(CostumerRepository costumerRepository) {
        this.costumerRepository = costumerRepository;
    }

    public Costumer createCostumer(Costumer costumer) {
        return costumerRepository.save(costumer);
    }

    public Costumer getCostumer(Long id) {
        return costumerRepository.findById(id).orElse(null);
    }

    public void deleteCostumer(Long id) {
        costumerRepository.deleteById(id);
    }

    public Costumer updateCostumer(Costumer costumer) {
        Optional<Costumer> costumerOptional = costumerRepository.findById(costumer.getId());
        if (costumerOptional.isEmpty()) {
            return null;
        }
        Costumer entity = costumerOptional.get();
        entity.setName(costumer.getName());
        entity.setEmail(costumer.getEmail());
        entity.setAddress(costumer.getAddress());
        entity.setPhone(costumer.getPhone());
        entity.setAge(costumer.getAge());
        return costumerRepository.save(entity);
    }

    public List<Costumer> getCostumers() {
        return costumerRepository.findAll();
    }
}
