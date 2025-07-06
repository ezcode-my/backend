document.addEventListener('DOMContentLoaded', function () {
    // 메뉴 버튼 active 처리
    const menuBtns = document.querySelectorAll('.menu-btn');
    menuBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            menuBtns.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
        });
    });

    fetchUserData();
});

function fetchUserData() {
    const accessToken = sessionStorage.getItem('accessToken');
    if (!accessToken) return;

    // 1. 리뷰 토큰 조회
    fetch('/api/users/review-token', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Authorization': accessToken
        },
        credentials: 'include'
    })
        .then(response => {
            if (!response.ok) throw new Error('유저 정보를 불러오지 못했습니다.');
            return response.json();
        })
        .then((res) => {
            // 응답 구조에 따라 아래 코드 수정
            const user = res.result || res;
            const aiReviewElem = document.getElementById('aiReview');
            if (aiReviewElem) aiReviewElem.textContent = user.reviewToken || '-';
        })
        .catch(err => {
            console.error(err);
        });


    // 1. 내 정보 조회 API 호출 (토큰을 Authorization 헤더에 넣기)
    fetch('/api/users', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            ...(accessToken ? {'Authorization': accessToken} : {})
        },
        credentials: 'include' // 필요하다면 유지
    })
        .then(response => {
            if (!response.ok) throw new Error('유저 정보를 불러오지 못했습니다.');
            return response.json();
        })
        .then((res) => {
            const user = res.result;
            // 2. 받아온 정보로 화면 반영
            document.getElementById('nickname').textContent = user.nickname || '';
            document.getElementById('profileImage').src = user.profileImageUrl || '/images/logo.png';
            document.getElementById('userId').textContent = user.username || '';
            document.getElementById('email').textContent = user.email || '';
            document.getElementById('tier').textContent = user.tier || '';
            document.getElementById('ranking').textContent = user.ranking || '-';
            document.getElementById('solvedCount').textContent = user.totalSolvedCount || '-';
            setEmailVerify(!!user.verified);

            // 블로그, 깃허브 버튼 링크 처리
            const githubBtn = document.querySelector('.github-btn');
            if (githubBtn && user.githubUrl) {
                githubBtn.onclick = () => window.open(user.githubUrl, '_blank');
                githubBtn.disabled = false;
            } else if (githubBtn) {
                githubBtn.disabled = true;
            }
            const blogBtn = document.querySelector('.blog-btn');
            if (blogBtn && user.blogUrl) {
                blogBtn.onclick = () => window.open(user.blogUrl, '_blank');
                blogBtn.disabled = false;
            } else if (blogBtn) {
                blogBtn.disabled = true;
            }
        })
        .catch(err => {
            console.error(err);
            // 필요시 에러 안내
        });

}

function setEmailVerify(isVerified) {
    const emailVerify = document.getElementById('emailVerify');
    if (isVerified) {
        emailVerify.innerHTML = `
            <img src="/images/check.png" alt="인증 완료" class="check-image" />
        `;
    } else {
        emailVerify.innerHTML = `
            <img src="/images/email-verify.png" 
                 alt="이메일 인증하기" 
                 class="email-verify clickable" 
                 id="emailVerifyButton" />
        `;

        // 이미지 삽입 후 클릭 이벤트 바인딩
        const emailVerifyButton = document.getElementById('emailVerifyButton');
        emailVerifyButton.addEventListener('click', sendEmailVerification);
    }
}

//이메일 전송
async function sendEmailVerification() {
    const accessToken = sessionStorage.getItem('accessToken');

    if (!accessToken) {
        alert('로그인이 필요합니다.');
        return;
    }

    try {
        const response = await fetch('/api/email/send', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': accessToken
            },
            body: JSON.stringify({
                redirectUrl: 'http://localhost:8080'
            })
        });

        if (response.ok) {
            const result = await response.json();
            alert('인증 메일이 발송되었습니다.');
            console.log(result);
        } else {
            const error = await response.json();
            alert(`오류: ${error.message || '인증 요청 실패'}`);
        }
    } catch (err) {
        console.error('이메일 인증 오류:', err);
        alert('서버 오류가 발생했습니다.');
    }
}

