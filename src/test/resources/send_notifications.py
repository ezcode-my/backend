# seed_notifications.py
import random
from datetime import datetime
from pymongo import MongoClient

MONGO_URI = "mongodb://root:1234@localhost:27017/codetest?authSource=admin"
DB_NAME = "codetest"  # 실제 DB 이름으로 변경
COLLECTION_NAME = "notifications"  # 실제 컬렉션 이름으로 변경

# payload 템플릿
base_payload = {
    "problemId": 1,
    "discussionId": 1,
    "replyId": 7,
    "authorId": 18,
    "authorNickname": "정직한펭귄908",
    "content": "대충 토론에 대한 댓글 내용",
    "_class": "org.ezcode.codetest.application.notification.event.payload.ReplyCreatePayload"
}

def random_email():
    # 1부터 10까지 랜덤, 1일 때는 ttest@test.com
    idx = random.randint(1, 10)
    return "ttest@test.com" if idx == 1 else f"ttest{idx}@test.com"

def gen_docs(count=100):
    docs = []
    for _ in range(count):
        docs.append({
            "createdAt": datetime.utcnow(),
            "isRead": False,
            "notificationType": "COMMUNITY_DISCUSSION_REPLY",
            "payload": base_payload,
            "principalName": random_email(),
            "_class": "org.ezcode.codetest.infrastructure.notification.model.NotificationDocument"
        })
    return docs

def main():
    client = MongoClient(MONGO_URI)
    db = client[DB_NAME]
    coll = db[COLLECTION_NAME]

    # docs = gen_docs(1000000)
    docs = gen_docs(100)
    result = coll.insert_many(docs)
    print(f"Inserted {len(result.inserted_ids)} documents into `{DB_NAME}.{COLLECTION_NAME}`")

if __name__ == "__main__":
    main()
