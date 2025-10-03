const selectedActors = new Map();
const allActors = [];

window.onload = function () {
    document.querySelectorAll('.actor-item').forEach(item => {
        allActors.push(item);
    });

    const queryParams = new URLSearchParams(window.location.search);
    const preSelectedIds = queryParams.get('preSelectedActorNos');
    if (preSelectedIds) {
        const ids = preSelectedIds.split(',').map(Number);
        ids.forEach(id => {
            const actorItem = document.querySelector(`.actor-item[data-actor-no="${id}"]`);
            if (actorItem) {
                toggleActorSelection(actorItem);
            }
        });
    }

    const searchInput = document.getElementById('actorSearchInput');
    if (searchInput) {
        searchInput.addEventListener('keyup', function(event) {
            if (event.key === 'Enter') {
                searchActors();
            }
        });
    }
};

function toggleActorSelection(element) {
    const actorNo = parseInt(element.dataset.actorNo);
    const actorName = element.dataset.actorName;
    const indicator = element.querySelector('.selection-indicator');

    if (selectedActors.has(actorNo)) {
        selectedActors.delete(actorNo);
        element.classList.remove('selected');
        indicator.style.display = 'none';
    } else {
        selectedActors.set(actorNo, actorName);
        element.classList.add('selected');
        indicator.style.display = 'flex';
    }
}

function searchActors() {
    const searchTerm = document.getElementById('actorSearchInput').value; // ariaValueMax -> value로 수정

    fetch(`/api/actors/search?keyword=${encodeURIComponent(searchTerm)}`)
        .then(response => response.json())
        .then(data => {
            const actorsListContainer = document.getElementById('actorsListContainer');
            actorsListContainer.innerHTML = '';

            if (data.length === 0) {
                actorsListContainer.innerHTML = '<div style="color:#aaa; text-align:center; width:100%;">검색 결과가 없습니다.</div>';
            } else {
                data.forEach(actor => {
                    const actorItem = document.createElement('div');
                    actorItem.className = 'actor-item';
                    actorItem.id = `actor-${actor.actorNo}`;
                    actorItem.dataset.actorNo = actor.actorNo;
                    actorItem.dataset.actorName = actor.actorName;
                    
                    actorItem.onclick = function() {
                        toggleActorSelection(this);
                    };

                    let actorPosterHtml = '';
                    if (actor.actorPoster) {
                        actorPosterHtml = `<img src="/img/${actor.actorPoster}" alt="${actor.actorName} 포스터" onerror="this.onerror=null; this.src='/img/default_actor.png';">`;
                    } else {
                        actorPosterHtml = `<img src="/img/default_actor.png" alt="${actor.actorName} 포스터">`;
                    }
                    
                    actorItem.innerHTML = `
                        ${actorPosterHtml}
                        <p>${actor.actorName}</p>
                        <div class="selection-indicator" style="display: none;">✔</div>
                    `;
                    actorsListContainer.appendChild(actorItem);

                    if (selectedActors.has(actor.actorNo)) {
                        actorItem.classList.add('selected');
                        actorItem.querySelector('.selection-indicator').style.display = 'flex';
                    }
                });
            }
        })
        .catch(error => {
            console.error('배우 검색 중 오류 발생:', error);
            document.getElementById('actorsListContainer').innerHTML = '<div style="color:#e50914; text-align:center; width:100%;">배우 검색에 실패했습니다.</div>';
        });
}

function sendSelectedActors() {
    const actorsToSend = Array.from(selectedActors.entries()).map(([no, name]) => ({
        actorNo: no,
        actorName: name
    }));

    if (window.opener && window.opener.receiveSelectedActors) {
        window.opener.receiveSelectedActors(actorsToSend);
    }
    window.close();
}