package com.kkanggogo.facealbum.unitTest.profile;

import com.kkanggogo.facealbum.profile.ProfileController;
import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProfileControllerUnitTest {

    @Test
    public void prod_profile_조회() {
        // given
        String expectedProfile = "prod";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("prod-cloud");
        env.addActiveProfile("prod-db");

        ProfileController controller = new ProfileController(env);

        // when
        String profile = controller.profile();

        // then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}
