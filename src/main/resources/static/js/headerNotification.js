// 헤더 알림 웹소켓 및 알림 모달 관련 코드

let headerStompClient;
let notifications = []; // 알림 목록 (최초 1페이지)
let unreadCount = 0;    // 읽지 않은 알림 개수

window.connectNotificationWS = function() {
    const tokenOnly = (sessionStorage.getItem('accessToken') || '').replace(/^Bearer /, '');
    if (!tokenOnly) return;
    const socket = new SockJS(`/ws?token=${encodeURIComponent(tokenOnly)}`);
    headerStompClient = Stomp.over(socket);

    headerStompClient.connect({}, () => {
        // 실시간 알림 이벤트 구독
        headerStompClient.subscribe('/user/queue/notification', msg => {
            const notif = JSON.parse(msg.body);
            notifications.unshift(notif); // 새 알림 맨 앞에 추가
            if (!notif.isRead) unreadCount++;
            updateNotificationUI();
        });

        // 알림 목록 구독 (최초 1회)
        headerStompClient.subscribe('/user/queue/notifications', msg => {
            const data = JSON.parse(msg.body);
            notifications = data.content || [];
            unreadCount = notifications.filter(n => !n.isRead).length;
            updateNotificationUI();
        });

        // 최초 알림 목록 요청 (REST API)
        fetch('/api/notifications?page=0', {
            headers: { 'Authorization': sessionStorage.getItem('accessToken') }
        });
    }, err => console.error('[STOMP] 연결 오류:', err));
}

function updateNotificationUI() {
    const btn = document.getElementById('notificationBtn');
    if (!btn) return;
    // 빨간 뱃지 표시
    if (unreadCount > 0) {
        btn.classList.add('has-unread');
    } else {
        btn.classList.remove('has-unread');
    }
    // 알림 모달이 열려 있다면 목록도 갱신
    renderNotificationModal();
}

function renderNotificationModal() {
    const listEl = document.getElementById('notificationList');
    if (!listEl) return;
    if (!notifications.length) {
        listEl.innerHTML = '<div style="color:#aaa;padding:16px;">알림이 없습니다.</div>';
        return;
    }
    listEl.innerHTML = notifications.map(n => `
        <div class="notification-item${n.isRead ? '' : ' unread'}">
            <div class="message">${n.message}</div>
            <div class="time">${formatTime(n.createdAt)}</div>
        </div>
    `).join('');
}

function formatTime(iso) {
    // 간단한 시간 포맷 (예: 10분 전, 하루 전 등)
    const d = new Date(iso);
    const now = new Date();
    const diff = (now - d) / 1000;
    if (diff < 60) return '방금 전';
    if (diff < 3600) return Math.floor(diff/60) + '분 전';
    if (diff < 86400) return Math.floor(diff/3600) + '시간 전';
    return d.toLocaleDateString();
}

window.bindHeaderNotificationEvents = function() {
    const notifBtn = document.getElementById('notificationBtn');
    if (notifBtn) {
        notifBtn.addEventListener('click', () => {
            const modal = document.getElementById('notificationModal');
            if (!modal) return;
            modal.style.display = (modal.style.display === 'block') ? 'none' : 'block';
            renderNotificationModal();
        });
    }
    const moreBtn = document.getElementById('notificationMoreBtn');
    if (moreBtn) {
        moreBtn.addEventListener('click', () => {
            window.location.href = '/test/notifications';
        });
    }
}
