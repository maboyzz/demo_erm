package com.nthuy.demo_erm.service;

import com.nthuy.demo_erm.dto.TrackingActionDTO;

import java.util.List;

public interface TrackingActionService {
    public void creates(List<TrackingActionDTO> dtos);
    public void create(List<TrackingActionDTO> dtos, Long reasonId);
}
