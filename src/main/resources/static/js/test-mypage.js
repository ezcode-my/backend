document.addEventListener('DOMContentLoaded', function() {
    // 메뉴 버튼 active 처리
    const menuBtns = document.querySelectorAll('.menu-btn');
    menuBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            menuBtns.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
        });
    });

    document.addEventListener('DOMContentLoaded', function() {
        fetch('/html/header.html')
            .then(res => res.text())
            .then(data => {
                document.getElementById('header-placeholder').innerHTML = data;
            });
    });

    document.addEventListener('DOMContentLoaded', function() {
        // 메뉴 버튼 active 처리
        const menuBtns = document.querySelectorAll('.menu-btn');
        menuBtns.forEach(btn => {
            btn.addEventListener('click', function() {
                menuBtns.forEach(b => b.classList.remove('active'));
                this.classList.add('active');
            });
        });

        // 1. 내 정보 조회 API 호출
        fetch('/api/users', {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
                // 필요하다면 인증 토큰 등 추가
            },
            credentials: 'include' // 세션/쿠키 인증 시 필요
        })
            .then(response => {
                if (!response.ok) throw new Error('유저 정보를 불러오지 못했습니다.');
                return response.json();
            })
            .then(user => {
                // 2. 받아온 정보로 화면 반영
                document.getElementById('nickname').textContent = user.nickname || '';
                document.getElementById('profileImage').src = user.profileImageUrl || '/default-profile.png';
                document.getElementById('username').textContent = user.username || '';
                document.getElementById('email').textContent = user.email || '';
                document.getElementById('tier').textContent = user.tier || '';
                // 이메일 인증 여부 표시
                document.getElementById('ranking').textContent = user.ranking || '-';
                document.getElementById('solvedCount').textContent = user.solvedCount || '-';
                document.getElementById('aiReview').textContent = user.aiReview || '-';
                // 이메일 인증 여부 표시 (체크 아이콘)
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
                // 추가 정보(소개 등) 필요시 여기에 추가
                // document.getElementById('introduction').textContent = user.introduction || '';
            })
            .catch(err => {
                console.error(err);
                // 필요시 에러 안내
            });
    });
// 이메일 인증 상태 표시 함수
    function setEmailVerify(isVerified) {
        const emailVerify = document.getElementById('emailVerify');
        if (isVerified) {
            emailVerify.innerHTML = `<span class="checkmark"><svg viewBox="0 0 20 20" fill="none"><circle cx="10" cy="10" r="10" fill="#00a141"/><path d="M6 10.5L9 13.5L14 8.5" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg></span>`;
        } else {
            emailVerify.textContent = '-';
        }
    }
});
