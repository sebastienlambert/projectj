package projectj.query.userprofile;


import java.util.UUID;

public interface UserProfileViewRepository {

    UserProfileView findOne(UUID userId);

    UserProfileView create(UserProfileView userProfile);
}
