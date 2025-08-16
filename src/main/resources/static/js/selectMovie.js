// 페이지 로드 후 스크립트 실행
document.addEventListener('DOMContentLoaded', function () {
    // 페이지에서 id가 toggleLikeButton인 요소를 찾아 toggleButton에 할당
    const toggleButton = document.getElementById('toggleLikeButton');

    // 좋아요 개수를 표시하는 span태그를 currentCountLikeMovie에 할당
    const currentCountLikeMovie = document.getElementById('countLikeMovie');

    // toggleButton의 존재 여부 확인
    if (toggleButton) {
        // toggleButton을 클릭했을때
        toggleButton.addEventListener('click', function () {

            // data-movieNo의 값을 movieNo에 할당
            const movieNo = this.dataset.movieNo;
            // data-isLiked의 타입, 값을 비교하여 isLiked에 할당
            let isLiked = this.dataset.isLiked === 'true';
            // isLiked가 true면 /likeMovieCancel을 false면 /likeMovie를 url에 할당
            // 영상에 추천이 눌러져있다면 버튼 클릭시 /likeMovieCancel을 요청해야함.
            const url = isLiked ? '/likeMovieCancel' : '/likeMovie';

            // buttonTextSpan에 toggleButton안의 span태그를 할당
            const buttonTextSpan = this.querySelector('span');

            this.disabled = true;

            // AJAX 요청
            fetch(url, {
                // JSON형식으로 movieNo, Number(movieNo)를 문자열로 변환해서 서버에 데이터 전송
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ movieNo: Number(movieNo) })
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

                        // data객체가 currentCountLikeMovie를 고유 속성으로 가지고 있는지 확인
                        if (data.hasOwnProperty('currentLikeCount')) {
                            // span태그 안에서 currentCountLikeMovie 변수 할당 성공여부 확인
                            if (currentCountLikeMovie) {
                                currentCountLikeMovie.textContent = data.currentLikeCount;
                            }
                        }
                        else {
                            // span 태그가 존재하는지 확인
                            if (currentCountLikeMovie) {
                                // 현재 화면에 표시된 개수를 파싱 (문자열 -> 숫자)
                                let currentCount = parseInt(currentCountLikeMovie.textContent);
                                // 이제 '좋아요' 상태라면 1증가
                                if (isLiked) {
                                    currentCount++;
                                } else { // 이제 '좋아요 취소' 상태라면 1 감소
                                    currentCount--;
                                }
                                currentCountLikeMovie.textContent = currentCount;
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

    // 리뷰 작성

    // reveiwForm안의 요소들을 reviewForm라는 변수에 할당
    const reviewForm = document.getElementById('reviewForm');
    // reviewContent안의 요소들을 reviewContentTextarea라는 변수에 할당
    const reviewCommentTextarea = document.getElementById('reviewComment');
    // reviewListContainer(기존의 리뷰 목록)안의 요소들을 reviewListContainer라는 변수에 할당
    const reviewListContainer = document.getElementById('reviewListContainer');
    // '리뷰 없음' 메세지를 담은 div를 변수 noReviewsMessage에 할당
    const noReviewsMessage = document.getElementById('noReviewsMessage');

    // reviewForm이 존재 할때만
    if (reviewForm) {
        // 폼 제출시 새로고침 안되도록
        reviewForm.addEventListener('submit', function (event) {
            event.preventDefault();

            // input에 hidden으로 설정한 movieNoInput의 value를 변수 movieNo에 할당
            const movieNo = document.getElementById('movieNoInput').value;
            // input에 hidden으로 설정한 userNo의 value를 변수 userNo에 할당
            const userNo = document.getElementById('userNoInput').value;
            // reviewContentTextara의 value에서 공백을 제거한 값을 변수 reviewContent에 할당
            const reviewComment = reviewCommentTextarea.value.trim();
            // user가 체크한 별점을 변수 reviewRating로 초기화
            let reviewRating = null;
            // 선택한 평점 버튼을 탐색하여 담기 위한 변수 ratingRadios
            const ratingRadios = document.querySelectorAll('input[name="reviewRating"]');
            // 평점을 user가 체크한 평점과 일치하는지 확인하기위한 반복문
            for (const radio of ratingRadios) {
                // 현재 radio변수에 할당된 버튼이 true라면
                if (radio.checked) {
                    // radio의 value를 reviewRating에 할당
                    reviewRating = radio.value;
                    break;
                }
            }

            // reviewContent가 비었다면
            if (!reviewComment) {
                // 리뷰 내용을 입력해주세요. 라는 경고메세지
                alert('리뷰 내용을 입력해주세요.');
                return;
            }
            // reviewRating이 null 이라면
            if (reviewRating === null) {
                // 평점을 선택해주세요. 라는 경고메세지
                alert('평점을 선택해주세요.');
                return;
            }

            // ReviewVo에 매핑될 객체를 JSON형태로 생성
            const reviewData = {
                reviewTarget: movieNo,
                userNo: userNo,
                reviewRating: reviewRating,
                reviewComment: reviewComment
            };

            // AJAX방식으로 데이터 전송
            fetch('/insertMovieReview', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(reviewData)
            })
                .then(response => {
                    // 서버 응답이 200이 아니라면
                    if (!response.ok) {
                        return response.text().then(text => {
                            throw new Error(`리뷰 전송 실패: ${response.status}${response.statusText}-${text}`);
                        });
                    }
                    return response.text();
                })
                .then(message => {
                    console.log('서버 응답:', message);
                    alert(message);

                    // 리뷰 내용과 평점 버튼 초기화
                    reviewCommentTextarea.value = '';
                    ratingRadios.forEach(radio => radio.checked = false);

                    // 리뷰 없었다가 새로 생겼을때 noReviewsMessage를 숨김.
                    if (noReviewsMessage) {
                        noReviewsMessage.style.display = 'none';
                    }
                })
                .catch(error => {
                    console.error('리뷰 작성 실패:', error);
                    alert('리뷰 작성 실패' + error.message);
                });

        });
    }

});

