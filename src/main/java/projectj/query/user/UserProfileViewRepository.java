/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.query.user;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserProfileViewRepository extends JpaRepository<UserProfileView, String> {
}
