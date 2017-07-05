package projectj.query.userprofile;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserProfileViewRepository extends JpaRepository<UserProfileView, UUID> {

}
