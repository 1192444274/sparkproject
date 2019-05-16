package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.UserPortrait;

public interface UserPortraitDAO {
    void insert(UserPortrait userPortrait);
    UserPortrait query(int id);
}
