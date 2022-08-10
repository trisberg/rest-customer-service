package com.springdeveloper.csapp.domain;

import com.springdeveloper.csapp.data.CustomerProfileEntity;
import com.springdeveloper.csapp.data.CustomerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerProfileService {

    private final CustomerProfileRepository repository;

    public CustomerProfileService(CustomerProfileRepository repository) {
        this.repository = repository;
    }

    public CustomerProfileResponse create(CustomerProfileCreateRequest dto) {
        var entity = new CustomerProfileEntity()
                .setId(UUID.randomUUID())
                .setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setEmail(dto.getEmail());

        var persistedEntity = repository.save(entity);
        return entityToDto(persistedEntity);
    }

    public Optional<CustomerProfileResponse> getById(String idRepresentation) {
        return safeConvertToUUID(idRepresentation)
                .flatMap(repository::findById)
                .map(this::entityToDto);
    }

    private Optional<UUID> safeConvertToUUID(String stringRepresentation) {
        try {
            return Optional.of(UUID.fromString(stringRepresentation));
        }
        catch (IllegalArgumentException ignorable) {
            return Optional.empty();
        }
    }

    private CustomerProfileResponse entityToDto(CustomerProfileEntity entity) {
        return new CustomerProfileResponse(
                entity.getId().toString(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail());
    }
}
