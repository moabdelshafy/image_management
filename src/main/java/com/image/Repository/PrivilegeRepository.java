package com.image.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.image.Model.Privilege;

import java.util.List;


@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Long>{

	Privilege findByName(String name);

    List<Privilege> findByNameIn(List<String> list);
}
