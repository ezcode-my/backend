// problem-search.js

let debounceTimer = null, focusIndex = -1;

function getToken() {
    return sessionStorage.getItem('accessToken');
}

function clearAutocomplete() {
    document.getElementById('autocomplete-list').innerHTML = "";
    focusIndex = -1;
}

async function fetchSuggestions(keyword) {
    const token = getToken();
    if (!keyword || !token) return;
    try {
        const resp = await fetch('/api/problems/suggestions?keyword=' + encodeURIComponent(keyword), {
            headers: {'Authorization': token.startsWith('Bearer ') ? token : 'Bearer ' + token}
        });
        const data = await resp.json();
        if (!data.success || !Array.isArray(data.result)) return;
        const listEl = document.getElementById('autocomplete-list');
        listEl.innerHTML = "";
        data.result.forEach((str, idx) => {
            const li = document.createElement('li');
            li.textContent = str;
            li.className = "autocomplete-item";
            li.dataset.index = idx;
            li.onclick = () => {
                document.getElementById('keyword').value = str;
                clearAutocomplete();
            };
            listEl.appendChild(li);
        });
    } catch {}
}

function handleKeywordInput(e) {
    const kw = e.target.value.trim();
    clearAutocomplete();
    if (kw.length < 2) return;
    if (debounceTimer) clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => fetchSuggestions(kw), 250);
}

function handleKeyDown(e) {
    const items = document.querySelectorAll('.autocomplete-item');
    if (!items.length) return;
    if (e.key === 'ArrowDown') {
        e.preventDefault();
        focusIndex = (focusIndex + 1) % items.length;
        updateFocus(items);
    } else if (e.key === 'ArrowUp') {
        e.preventDefault();
        focusIndex = (focusIndex - 1 + items.length) % items.length;
        updateFocus(items);
    } else if (e.key === 'Enter' && focusIndex >= 0) {
        e.preventDefault();
        document.getElementById('keyword').value = items[focusIndex].textContent;
        clearAutocomplete();
    }
}

function updateFocus(items) {
    items.forEach((it, idx) => it.classList.toggle('active', idx === focusIndex));
}

async function handleSearchSubmit(e) {
    e.preventDefault();
    const token = getToken();
    const kw = document.getElementById('keyword').value.trim();
    if (kw.length < 2 || !token) return;

    const resultsDiv = document.getElementById('results');
    resultsDiv.innerHTML = '<div class="loader">⏳</div>';
    await new Promise(r => setTimeout(r, 1000));

    try {
        const resp = await fetch('/api/problems/search?keyword=' + encodeURIComponent(kw), {
            method: 'GET',
            headers: {'Authorization': token.startsWith('Bearer ') ? token : 'Bearer ' + token}
        });
        const data = await resp.json();

        let html = '<span class="result-title">PROBLEM LIST :</span>';
        if (data.success && Array.isArray(data.result) && data.result.length) {
            html += '<div>';
            data.result.forEach(item => {
                html += `
              <div class="result-item">
                <div><b>ID:</b> ${item.id}</div>
                <div><b>Title:</b> <a href="/test/submit?problemId=${item.id}">${item.title}</a></div>
                <div><b>Category:</b> ${item.category}</div>
                <div><b>Difficulty:</b> ${item.difficulty}</div>
                <div><b>Reference:</b> ${item.reference}</div>
                <div><b>Description:</b> ${item.description}</div>
                <div><b>Score:</b> ${item.score}</div>
              </div>`;
            });
            html += '</div>';
        } else {
            html += '<div style="margin-top:12px; color: #aaa;">검색 결과가 없습니다.</div>';
        }
        resultsDiv.innerHTML = html;
    } catch {
        resultsDiv.innerHTML = '<div style="color:red;">검색 중 오류가 발생했습니다.</div>';
    } finally {
        clearAutocomplete();
    }
}

function initProblemSearch() {
    clearAutocomplete();
    const ki = document.getElementById('keyword');
    ki.addEventListener('input', handleKeywordInput);
    ki.addEventListener('keydown', handleKeyDown);
    document.getElementById('searchForm').addEventListener('submit', handleSearchSubmit);
}