package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.FunnelMessage;

import java.util.List;

public interface FunnelMessageDAO {
    List<FunnelMessage> findAllFunnel();
}
