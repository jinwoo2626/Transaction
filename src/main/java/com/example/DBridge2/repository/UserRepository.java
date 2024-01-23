package com.example.DBridge2.repository;

import com.example.DBridge2.config.dto.UserInfoDTO;
import com.example.DBridge2.domain.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//CRUD 함수를 JPARepository가 들고있음.
//@Repository라는 어노테이션이 없어도 IOC가 된다. (빈등록) / JPARepository를 상속하고 있기 때문에
public interface UserRepository extends JpaRepository<Users, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<Users> findOneWithAuthoritiesByUsername(String username);

    UserInfoDTO findByUsername(String username);

    //findBy규칙 -> username문법
    //select * from user whete username = 1?
//    public Users findByUsername(String username);

    Users findByEmailAndPassword(String email, String password);
    Optional<Users> findByEmail(String email);
    boolean existsUsersByEmail(String email);
}
