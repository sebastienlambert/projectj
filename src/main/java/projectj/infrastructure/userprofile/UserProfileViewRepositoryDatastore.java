package projectj.infrastructure.userprofile;


import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projectj.query.userprofile.UserProfileView;
import projectj.query.userprofile.UserProfileViewRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UserProfileViewRepositoryDatastore implements UserProfileViewRepository {


    private Datastore datastore;
    private KeyFactory userProfileKeyFactory;
    private UserProfileViewMapper userProfileViewMapper;

    @Autowired
    public UserProfileViewRepositoryDatastore(Datastore datastore) {
        this.datastore = datastore;
        this.userProfileKeyFactory = datastore.newKeyFactory().setKind("UserProfileView");
        this.userProfileViewMapper = new UserProfileViewMapper(userProfileKeyFactory);
    }

    @Override
    public UserProfileView findOne(UUID userId) {
        Key key = userProfileKeyFactory.newKey(userId.toString());
        Entity entity = datastore.get(key);
        return userProfileViewMapper.toUserProfileView(entity);
    }

    @Override
    public UserProfileView create(UserProfileView userProfile) {
        userProfile = userProfile.withCreatedDate(LocalDateTime.now());
        Entity entity = userProfileViewMapper.toEntity(userProfile);
        datastore.add(entity);
        return userProfile;
    }


}
