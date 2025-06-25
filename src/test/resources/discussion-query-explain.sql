# 기존 코드
EXPLAIN
SELECT
    d1_0.id,
    d1_0.user_id,
    u1_0.nickname,
    u1_0.tier,
    u1_0.profile_image_url,
    d1_0.problem_id,
    d1_0.content,
    d1_0.created_at,
    (SELECT count(dv1_0.id) FROM discussion_vote dv1_0 WHERE dv1_0.discussion_id = d1_0.id AND dv1_0.vote_type = 'UP'),
    (SELECT count(dv2_0.id) FROM discussion_vote dv2_0 WHERE dv2_0.discussion_id = d1_0.id AND dv2_0.vote_type = 'DOWN'),
    (SELECT count(r1_0.id) FROM reply r1_0 WHERE r1_0.discussion_id = d1_0.id),
    (SELECT dv3_0.vote_type FROM discussion_vote dv3_0 WHERE dv3_0.discussion_id = d1_0.id AND dv3_0.voter_id = 1)
FROM
    discussion d1_0
        JOIN
    users u1_0 ON u1_0.id = d1_0.user_id
WHERE
    d1_0.problem_id = 500
ORDER BY
    ((SELECT count(dv4_0.id) FROM discussion_vote dv4_0 WHERE dv4_0.discussion_id = d1_0.id AND dv4_0.vote_type = 'UP')
        -
     (SELECT count(dv5_0.id) FROM discussion_vote dv5_0 WHERE dv5_0.discussion_id = d1_0.id AND dv5_0.vote_type = 'DOWN')) DESC,
    d1_0.id DESC
    LIMIT 10 OFFSET 0;

# 1차 개선
EXPLAIN
SELECT
    d1_0.id,
    u1_0.id,
    u1_0.nickname,
    u1_0.tier,
    u1_0.profile_image_url,
    d1_0.problem_id,
    d1_0.content,
    d1_0.created_at,
    count(distinct case when (dv1_0.vote_type = 'UP') then dv1_0.id else null end),
    count(distinct case when (dv1_0.vote_type = 'DOWN') then dv1_0.id else null end),
    count(distinct r1_0.id),
    max(case when (dv1_0.voter_id = 1) then dv1_0.vote_type else null end)
FROM
    discussion d1_0
        JOIN
    users u1_0
    ON d1_0.user_id = u1_0.id
        LEFT JOIN
    discussion_vote dv1_0
    ON dv1_0.discussion_id = d1_0.id
        LEFT JOIN
    reply r1_0
    ON r1_0.discussion_id = d1_0.id
WHERE
    d1_0.problem_id = 500
GROUP BY
    d1_0.id
ORDER BY
    (count(distinct case when (dv1_0.vote_type = 'UP') then dv1_0.id else null end)
        -
     count(distinct case when (dv1_0.vote_type = 'DOWN') then dv1_0.id else null end)) DESC,
    d1_0.id DESC
    LIMIT 10 OFFSET 0;

# 2차 개선