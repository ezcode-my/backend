// /js/headerUtils.js
function loadHeaderWithAuthCheck() {
    fetch('/html/header.html')
        .then(res => res.text())
        .then(data => {
            // 헤더 삽입
            document.getElementById('header-placeholder').innerHTML = data;

            // header가 삽입된 후 connectNotificationWS가 있으면 실행
            if (window.connectNotificationWS) window.connectNotificationWS();
            if (window.bindHeaderNotificationEvents) window.bindHeaderNotificationEvents();

            // 삽입 이후에 요소들 찾아야 함
            const accessToken = sessionStorage.getItem("accessToken");

            const loginBtn = document.getElementById("loginBtn");
            const userMenu = document.getElementById("userMenu");
            const userIcon = document.getElementById("userIcon");
            const userDropdown = document.getElementById("userDropdown");
            const logoutBtn = document.getElementById("logoutBtn");

            if (accessToken && loginBtn && userMenu) {
                loginBtn.style.display = "none";
                userMenu.style.display = "block";
            }

            if (userIcon && userDropdown) {
                userIcon.addEventListener("click", () => {
                    userDropdown.style.display =
                        userDropdown.style.display === "block" ? "none" : "block";
                });
            }

            if (logoutBtn) {
                logoutBtn.addEventListener("click", () => {
                    sessionStorage.removeItem("accessToken");
                    window.location.href = "/test/signin";
                });
            }

            // 바깥 클릭 시 드롭다운 닫기
            document.addEventListener("click", (e) => {
                if (userMenu && !userMenu.contains(e.target)) {
                    userDropdown.style.display = "none";
                }
            });

            if (logoutBtn) {
                logoutBtn.addEventListener("click", async () => {
                    const accessToken = sessionStorage.getItem("accessToken");

                    if (accessToken) {
                        try {
                            await fetch("/logout", {
                                method: "POST",
                                headers: {
                                    "Content-Type": "application/json",
                                    "Authorization": accessToken
                                }
                            });
                        } catch (err) {
                            console.error("로그아웃 요청 실패:", err);
                            // 실패해도 일단 토큰 제거는 강제로 진행
                        }
                    }

                    // 클라이언트 토큰 제거
                    sessionStorage.clear();

                    // 로그인 페이지로 이동
                    window.location.href = "/test/main";
                });
            }
        })
        .catch(err => {
            console.error("헤더 불러오기 실패:", err);
        });
}
