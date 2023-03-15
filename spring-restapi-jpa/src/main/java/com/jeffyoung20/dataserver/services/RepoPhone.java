package com.jeffyoung20.dataserver.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeffyoung20.dataserver.models.data.Phone;

public interface RepoPhone extends JpaRepository<Phone,Long>{

}
