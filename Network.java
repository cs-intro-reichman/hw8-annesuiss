public class Network {

    // Fields
    private User[] users;  // The users in this network (an array of User objects)
    private int userCount; // Actual number of users in this network

    /** Creates a network with a given maximum number of users. */
    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    /** Creates a network with some users. The only purpose of this constructor is 
     *  to allow testing the toString and getUser methods, before implementing other methods. */
    public Network(int maxUserCount, boolean gettingStarted) {
        this(maxUserCount);
        if (gettingStarted) {
            users[0] = new User("Foo");
            users[1] = new User("Bar");
            users[2] = new User("Baz");
            userCount = 3;
        }
    }

    public int getUserCount() {
        return this.userCount;
    }

    /** Finds in this network, and returns, the user that has the given name.
     *  If there is no such user, returns null.
     */
    public User getUser(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equals(name)) {
                return users[i];
            }
        }
        return null;
    }

    /** Adds a new user with the given name to this network.
     *  If this network is full, does nothing and returns false;
     *  If the given name is already a user in this network, does nothing and returns false;
     *  Otherwise, creates a new user with the given name, adds the user to this network, and returns true. */
    public boolean addUser(String name) {
        if (userCount >= users.length || getUser(name) != null) {
            return false;
        }
        users[userCount] = new User(name);
        userCount++;
        return true;
    }

    /** Makes the user with name1 follow the user with name2. If successful, returns true.
     *  If any of the two names is not a user in this network,
     *  or if the "follows" addition failed for some reason, returns false. */
    public boolean addFollowee(String name1, String name2) {
        User user1 = getUser(name1);
        User user2 = getUser(name2);
        if (user1 == null || user2 == null) {
            return false;
        }
        return user1.addFollowee(name2);
    }

    /** For the user with the given name, recommends another user to follow. The recommended user is
     *  the user that has the maximal mutual number of followees as the user with the given name. */
    public String recommendWhoToFollow(String name) {
        User user = getUser(name);
        if (user == null) {
            return null;
        }
        
        User recommendation = null;
        int maxMutual = 0;

        for (int i = 0; i < userCount; i++) {
            User potential = users[i];
            if (potential.equals(user) || user.follows(potential.getName())) {
                continue;
            }

            int mutualFollowees = user.getMutualFolloweeCount(potential);
            if (mutualFollowees > maxMutual) {
                maxMutual = mutualFollowees;
                recommendation = potential;
            }
        }
        return (recommendation != null) ? recommendation.getName() : null;
    }

    /** Computes and returns the name of the most popular user in this network: 
     *  The user who appears the most in the follow lists of all the users. */
    public String mostPopularUser() {
        String mostPopular = null;
        int maxCount = 0;

        for (int i = 0; i < userCount; i++) {
            int count = followeeCount(users[i].getName());
            if (count > maxCount) {
                maxCount = count;
                mostPopular = users[i].getName();
            }
        }
        return mostPopular;
    }

    /** Returns the number of times that the given name appears in the follows lists of all
     *  the users in this network. Note: A name can appear 0 or 1 times in each list. */
    private int followeeCount(String name) {
        int count = 0;
        for (int i = 0; i < userCount; i++) {
            if (users[i].follows(name)) {
                count++;
            }
        }
        return count;
    }

    // Returns a textual description of all the users in this network, and who they follow.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Network:\n");
        for (int i = 0; i < userCount; i++) {
            sb.append(users[i].toString()).append("\n");
        }
        return sb.toString();
    }
}