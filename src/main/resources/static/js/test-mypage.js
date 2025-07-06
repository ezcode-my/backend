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
            document.getElementById('solvedCount').textContent = user.solvedCount || '-';
            document.getElementById('aiReview').textContent = user.aiReview || '-';
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

// 이메일 인증 상태 표시 함수
function setEmailVerify(isVerified) {
    const emailVerify = document.getElementById('emailVerify');
    if (isVerified) {
        emailVerify.innerHTML = `<span class="checkmark"><svg viewBox="0 0 20 20" fill="none"><circle cx="10" cy="10" r="10" fill="#00a141"/><path d="M6 10.5L9 13.5L14 8.5" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg></span>`;
    } else {
        emailVerify.textContent = '-';
    }
}
