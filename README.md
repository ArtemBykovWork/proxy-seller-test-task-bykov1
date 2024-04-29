## Artem Bykov Test Task

### Task

We need to write an API for an analog of Twitter. The user can register, log in, log out, leave a post, see his feed (a list of posts by people he follows), see another user's feed (without his subscriptions), comment on a post, leave or remove a like on a post (his own or someone else's), and follow another user.

### Queries

- Create a user
- Edit a user
- Delete a user
- Create a post
- Editing a post
- Deleting a post
- Leave/delete a post favorite
- Subscription to a user (the subscriber's feed will receive posts from the user to whom he/she subscribed)
- Unsubscribe from a user
- Commenting on a post
- Get a user's feed (including likes and comments)
- Get another user's feed
- Get post comments
- Likes can be left and deleted.

### How to run application
1. ./gradlew build  
2.  docker-compose up --build
3. Postman collection is available Proxy-Seller Test.postman_collection.json







