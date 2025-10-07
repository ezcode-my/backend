mongosh --port 10021 <<EOF
use admin;
db.createUser({
    user: "mongo",
    pwd: "mongo123",
    roles: [
      {
        role: "dbOwner",
        db: "ezcode",
      },
    ],
});
db.getUsers();
EOF