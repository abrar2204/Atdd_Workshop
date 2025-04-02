package com.tw.atdd_workshop.services;

import org.springframework.stereotype.Service;

@Service
public class FeatureToggleService {
    
    public Boolean IsOn(String featureName){
        return true;
    }
}