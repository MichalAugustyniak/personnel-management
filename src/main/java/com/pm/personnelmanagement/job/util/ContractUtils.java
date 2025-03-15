package com.pm.personnelmanagement.job.util;

import com.pm.personnelmanagement.job.exception.ContractNotFoundException;
import com.pm.personnelmanagement.job.model.Contract;
import com.pm.personnelmanagement.job.repository.ContractRepository;
import com.pm.personnelmanagement.user.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ContractUtils {
    private final ContractRepository contractRepository;

    public ContractUtils(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public Contract fetchContract(@NotNull UUID uuid) {
        return contractRepository.findByUuid(uuid).orElseThrow(
                () -> new ContractNotFoundException(
                        String.format("Contract of uuid %s not found", uuid)
                )
        );
    }

    public Contract fetchContract(@NotNull User user) {
        return contractRepository.findByUser(user).orElseThrow(
                () -> new ContractNotFoundException(
                        String.format("Contract for user %s not found", user.getUuid())
                )
        );
    }
}
