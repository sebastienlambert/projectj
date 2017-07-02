package projectj.infrastructure.userprofile;


import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import lombok.AllArgsConstructor;
import projectj.query.userprofile.UserProfileView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@AllArgsConstructor
public class UserProfileViewMapper {
    static final String ATTR_NICKNAME = "nickname";
    static final String ATTR_EMAIL = "email";
    static final String ATTR_CREATED_DATE = "createdDate";

    private KeyFactory userProfileKeyFactory;

    public Entity toEntity(UserProfileView userProfile) {
        Key key = userProfileKeyFactory.newKey(userProfile.getUserId().toString());
        Entity entity = Entity.newBuilder(key)         // Convert Book to an Entity
                .set(ATTR_NICKNAME, userProfile.getNickname())
                .set(ATTR_EMAIL, userProfile.getEmail())
                .set(ATTR_CREATED_DATE, userProfile.getCreatedDate().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
        return entity;
    }

    public UserProfileView toUserProfileView(Entity entity) {
        return UserProfileView.builder()
                .userId(UUID.fromString(entity.getKey().getName()))
                .createdDate(LocalDateTime.parse(entity.getString(ATTR_CREATED_DATE), DateTimeFormatter.ISO_DATE_TIME))
                .nickname(entity.getString(ATTR_NICKNAME))
                .email(entity.getString(ATTR_EMAIL))
                .build();
    }
}
