let selectedActorsDisplay;
let selectedActorNosInput;
const selectedActorsMap = new Map();
let isSubmitting = false;

document.addEventListener('DOMContentLoaded', (event) => {
    selectedActorsDisplay = document.getElementById('selectedActorsDisplay');
    selectedActorNosInput = document.getElementById('selectedActorNosInput');
    updateSelectedActorsDisplay();

    //
    
    //
});

function openActorSelection(event) {
    event.preventDefault();

    const selectionUrl = '/selectAllActor';
    const selectionUrlName = 'selectAllActorPopup';
    const selectionUrlFeatures = 'width=800, height=600, scrollbars=yes, resizable=yes';

    const preSelectedActorNos = Array.from(selectedActorsMap.keys()).join(',');
    const finalUrl = `${selectionUrl}?preSelectedActorNos=${preSelectedActorNos}`;

    window.open(finalUrl, selectionUrlName, selectionUrlFeatures);
}

function receiveSelectedActors(actors) {
    selectedActorsMap.clear(); 
    
    actors.forEach(actor => {
        selectedActorsMap.set(actor.actorNo, actor.actorName);
    });
    
    updateSelectedActorsDisplay(); 
}

function updateSelectedActorsDisplay() {
    selectedActorsDisplay.innerHTML = '';

    const actorNos = [];

    if (selectedActorsMap.size === 0) {
        selectedActorsDisplay.innerHTML = '<span style="color:#aaa;">선택된 배우 없음</span>';
    } else {
        selectedActorsMap.forEach((actorName, actorNo) => {
            const tag = document.createElement('span');
            tag.className = 'selected-actor-tag';

            tag.innerHTML = `${actorName} <button type="button" class="remove-actor-btn" data-actor-no="${actorNo}">x</button>`;

            selectedActorsDisplay.appendChild(tag);

            actorNos.push(actorNo); 
        });
    }

    selectedActorNosInput.value = actorNos.join(',');

    selectedActorsDisplay.querySelectorAll('.remove-actor-btn').forEach(button => {
        button.onclick = removeActor;
    });
}

function removeActor(event) {
    const actorNoToRemove = parseInt(event.target.dataset.actorNo);
    
    if (selectedActorsMap.has(actorNoToRemove)) {
        selectedActorsMap.delete(actorNoToRemove);
        updateSelectedActorsDisplay();
    }
}
