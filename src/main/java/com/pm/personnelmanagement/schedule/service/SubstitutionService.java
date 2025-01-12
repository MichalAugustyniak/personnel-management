package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;

public interface SubstitutionService {
    SubstitutionDTO getSubstitution(SubstitutionRequest dto);

    SubstitutionsDTO getSubstitutions(FetchSubstitutionsFiltersDTO dto);

    SubstitutionCreationResponse createSubstitution(SubstitutionCreationRequest dto);

    void updateSubstitution(SubstitutionUpdateRequest dto);

    void deleteSubstitution(SubstitutionDeletionRequest dto);
}
