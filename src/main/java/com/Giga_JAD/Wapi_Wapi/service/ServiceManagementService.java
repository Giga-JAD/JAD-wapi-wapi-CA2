package com.Giga_JAD.Wapi_Wapi.service;

import com.Giga_JAD.Wapi_Wapi.dto.ServiceRequest;
import java.util.List;

public interface ServiceManagementService {
    List<ServiceRequest> getAllServices();
}