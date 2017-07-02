package projectj.infrastructure.userprofile;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projectj.query.userprofile.UserProfileView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static projectj.infrastructure.userprofile.UserProfileViewMapper.*;

public class UserProfileViewRepositoryDatastoreTest {

    private UserProfileViewRepositoryDatastore userProfileViewRepositoryDatastore;

    @Mock
    private Datastore datastore;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        KeyFactory keyFactory = new KeyFactory("projectj");
        when(datastore.newKeyFactory()).thenReturn(keyFactory);
        userProfileViewRepositoryDatastore = new UserProfileViewRepositoryDatastore(datastore);
    }

    @Test
    public void testCreate() {
        LocalDateTime before = LocalDateTime.now();
        UserProfileView userProfileView = UserProfileView.builder()
                .userId(UUID.fromString("1bcff45c-db18-477a-b036-a2411ad75efa"))
                .nickname("fred")
                .email("fred.flinststone@bedrock.net")
                .build();
        UserProfileView savedUserProfileView = userProfileViewRepositoryDatastore.create(userProfileView);
        LocalDateTime after = LocalDateTime.now();

        assertEquals("1bcff45c-db18-477a-b036-a2411ad75efa", savedUserProfileView.getUserId().toString());
        assertEquals("fred", savedUserProfileView.getNickname());
        assertEquals("fred.flinststone@bedrock.net", savedUserProfileView.getEmail());
        assertTrue(savedUserProfileView.getCreatedDate().compareTo(before) >= 0);
        assertTrue(savedUserProfileView.getCreatedDate().compareTo(after) <= 0);

        Entity savedEntity = getSavedEntity();
        assertEquals("1bcff45c-db18-477a-b036-a2411ad75efa", savedEntity.getKey().getName());
        assertEquals("fred", savedEntity.getString(ATTR_NICKNAME));
        assertEquals("fred.flinststone@bedrock.net", savedEntity.getString(ATTR_EMAIL));
        assertEquals(savedUserProfileView.getCreatedDate().format(DateTimeFormatter.ISO_DATE_TIME),
                savedEntity.getString(ATTR_CREATED_DATE));
    }

    @Test
    public void testFindOne() {
        Key key = Key.newBuilder("projectj", "UserProfileView", "f6ea946b-c2d9-4499-845a-ca35cb57a8c0").build();
        Entity entity = Entity.newBuilder(key)
                .setKey(key)
                .set(ATTR_NICKNAME, "homer")
                .set(ATTR_EMAIL, "homer.simpson@fox.net")
                .set(ATTR_CREATED_DATE, "2017-07-02T16:10:09.654")
                .build();
        when(datastore.get(key)).thenReturn(entity);

        UserProfileView userProfileView = userProfileViewRepositoryDatastore.findOne(UUID.fromString("f6ea946b-c2d9-4499-845a-ca35cb57a8c0"));

        assertEquals("f6ea946b-c2d9-4499-845a-ca35cb57a8c0", userProfileView.getUserId().toString());
        assertEquals("homer", userProfileView.getNickname());
        assertEquals("homer.simpson@fox.net", userProfileView.getEmail());
        assertEquals("2017-07-02T16:10:09.654",
                userProfileView.getCreatedDate().format(DateTimeFormatter.ISO_DATE_TIME));
    }


    private Entity getSavedEntity() {
        ArgumentCaptor<Entity> argumentCaptor = ArgumentCaptor.forClass(Entity.class);
        verify(datastore, times(1)).add(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

}