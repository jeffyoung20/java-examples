package com.jeffyoung20.dataserver.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeffyoung20.dataserver.models.data.Phone;

public interface PhoneRepo extends JpaRepository<Phone,Long>{

}
