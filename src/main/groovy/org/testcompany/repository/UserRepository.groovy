package org.testcompany.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.testcompany.entity.User

interface UserRepository extends MongoRepository<User, String> {}