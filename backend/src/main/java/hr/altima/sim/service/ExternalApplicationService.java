package hr.altima.sim.service;

import hr.altima.sim.model.ExternalApplication;
import hr.altima.sim.repository.ExternalApplicationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalApplicationService {

    private ExternalApplicationRepository externalApplicationRepository;

    @Autowired
    public ExternalApplicationService(ExternalApplicationRepository externalApplicationRepository) {
        this.externalApplicationRepository = externalApplicationRepository;
    }

    public List<ExternalApplication> getList() {
        return externalApplicationRepository.findAll();
    }

    public ExternalApplication findByCode(String code) {
        return externalApplicationRepository.findByCode(code);
    }
}
