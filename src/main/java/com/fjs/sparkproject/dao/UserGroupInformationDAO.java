package com.fjs.sparkproject.dao;

import com.fjs.sparkproject.domain.UserGroupInformation;

public interface UserGroupInformationDAO {

    UserGroupInformation findByGroupNameAndWebsiteId(String groupName, Integer websiteId);
}
