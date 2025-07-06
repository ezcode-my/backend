function loadFooter() {
    fetch('/html/footer.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('footer-placeholder').innerHTML = data;
        })
        .catch(error => {
            console.error('푸터 로딩 실패:', error);
        });
}