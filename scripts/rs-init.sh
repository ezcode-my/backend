mongosh --port 10021 <<EOF
use ezcode;
var config = {
    "_id": "rs01",
    "version": 1,
    "members": [
        {
            "_id": 1,
            "host": "rs01p:10021",
            "priority": 2
        },
        {
            "_id": 2,
            "host": "rs01s:10022",
            "priority": 1
        },
        {
            "_id": 3,
            "host": "rs01a:10023",
            "priority": 0
        }
    ]
};
rs.initiate(config);
rs.status();
db.setProfilingLevel(2);
db.getProfilingStatus();
EOF