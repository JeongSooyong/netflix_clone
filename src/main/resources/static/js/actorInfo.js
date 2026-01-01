// 페이지 로드 후 스크립트 실행
document.addEventListener('DOMContentLoaded', function () {
    // 페이지에서 id가 toggleLikeButton인 요소를 찾아 toggleButton에 할당
    const toggleButton = document.getElementById('toggleLikeButton');

    // 좋아요 개수를 표시하는 span태그를 currentCountLikeActor에 할당
    const currentCountLikeActor = document.getElementById('countLikeActor');

    // toggleButton의 존재 여부 확인
    if (toggleButton) {
        // toggleButton을 클릭했을때
        toggleButton.addEventListener('click', function () {

            // data-actorNo의 값을 actorNo에 할당
            const actorNo = this.dataset.actorNo;
            // data-isLiked의 타입, 값을 비교하여 isLiked에 할당
            let isLiked = this.dataset.isLiked === 'true';
            // isLiked가 true면 /likeActorCancel을 false면 /likeActor를 url에 할당
            // 영상에 추천이 눌러져있다면 버튼 클릭시 /likeActorCancel을 요청해야함.
            const url = isLiked ? '/likeActorCancel' : '/likeActor';

            // buttonTextSpan에 toggleButton안의 span태그를 할당
            const buttonTextSpan = this.querySelector('span');

            this.disabled = true;

            // AJAX 요청
            fetch(url, {
                // JSON형식으로 actorNo, Number(actorNo)를 문자열로 변환해서 서버에 데이터 전송
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ actorNo: Number(actorNo) })
            })
                .then(response => {
                    // response.ok는 서버의 응답이 200~299인지 확인하는 코드
                    // 서버의 응답이 200~299가 아니라면 서버가 보낸 에러 JSON을 읽어 Error를 던짐
                    if (!response.ok) {
                        return response.json().then(errorData => {
                            throw new Error(errorData.message || '서버 오류 발생');
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    // 처리에 성공했을때(추천이 성공적으로 완료됐을때)
                    if (data.success) {
                        // isLiked에 !isLiked를 할당
                        isLiked = !isLiked;
                        this.dataset.isLiked = isLiked ? 'true' : 'false'; // data 속성 업데이트

                        buttonTextSpan.textContent = isLiked ? '추천 취소' : '추천'; // 버튼 텍스트 변경
                        this.classList.toggle('btn-unlike', isLiked);
                        this.classList.toggle('btn-like', !isLiked);

                        // data객체가 currentCountLikeActor를 고유 속성으로 가지고 있는지 확인
                        if (data.hasOwnProperty('currentCountLikeActor') && currentCountLikeActor) {
                            // span태그 안에서 currentCountLikeActor 변수 할당 성공여부 확인
                            if (currentCountLikeActor) {
                                currentCountLikeActor.textContent = data.currentCountLikeActor;
                            }
                        }
                        else {
                            // span 태그가 존재하는지 확인
                            if (currentCountLikeActor) {
                                // 현재 화면에 표시된 개수를 파싱 (문자열 -> 숫자)
                                let currentCount = parseInt(currentCountLikeActor.textContent);
                                // 이제 '좋아요' 상태라면 1증가
                                if (isLiked) {
                                    currentCount++;
                                } else { // 이제 '좋아요 취소' 상태라면 1 감소
                                    currentCount--;
                                }
                                currentCountLikeActor.textContent = currentCount;
                            }
                        }

                        // 사용자에게 성공 메시지 표시
                        alert(data.message);
                    } else {
                        alert('작업 실패: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('AJAX 오류:', error);
                    alert('오류가 발생했습니다: ' + error.message);
                })
                .finally(() => {
                    this.disabled = false;
                })
        });
    }

});

