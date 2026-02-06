package com.SIMOD.SIMOD.dto.auth;

import com.SIMOD.SIMOD.dto.endereco.AddressRequest;
import com.SIMOD.SIMOD.dto.familia.FamilyRequest;

import java.util.List;

public record CompleteProfileRequest(AddressRequest addressRequest,List<FamilyRequest> familyRequests) {

}
