package projectj.query.user;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserViewRepository extends JpaRepository<UserView, UUID> {

}
